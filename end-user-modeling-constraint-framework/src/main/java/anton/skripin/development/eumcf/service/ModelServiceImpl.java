package anton.skripin.development.eumcf.service;

import anton.skripin.development.domain.model.Association;
import anton.skripin.development.domain.model.Attribute;
import anton.skripin.development.domain.model.ModelElement;
import anton.skripin.development.eumcf.service.api.InstanceService;
import anton.skripin.development.eumcf.service.api.ModelService;
import anton.skripin.development.exception.InstanceOperationException;
import anton.skripin.development.mapper.ModelMapper;
import modicio.core.Registry;
import modicio.core.Rule;
import modicio.core.TypeHandle;
import modicio.core.rules.AssociationRule;
import modicio.core.rules.AttributeRule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import scala.Option;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static anton.skripin.development.eumcf.util.ScalaToJavaMapper.future;
import static anton.skripin.development.eumcf.util.ScalaToJavaMapper.set;

@Service
public class ModelServiceImpl implements ModelService {

    private final Registry registry;

    private final InstanceService instanceService;

    private final ModelMapper<modicio.core.ModelElement> modelMapper;


    /**
     * Constructor.
     *
     * @param registry        {@link Registry}
     * @param instanceService {@link InstanceService}
     * @param modelMapper     {@link ModelMapper<modicio.core.ModelElement>}
     */
    public ModelServiceImpl(Registry registry, InstanceService instanceService, ModelMapper<modicio.core.ModelElement> modelMapper) {
        this.registry = registry;
        this.instanceService = instanceService;
        this.modelMapper = modelMapper;
    }


    @Override
    public boolean updateAttributeDefinition(String uuid, String type, String attributeUuid, String key, String datatype) {
        try {
            AttributeRule newAttribute = AttributeRule.create(key, datatype, false, Option.empty());

            TypeHandle typeHandle = future(registry.getType(type, uuid)).get().get();


            modicio.core.ModelElement attributeDefinitionType = findTypeInHierarchyByAttributeDefinition(typeHandle.getModelElement(), attributeUuid);
            AttributeRule ruleToBeRemoved = set(attributeDefinitionType.deepAttributeRuleSet())
                    .stream()
                    .filter(attributeRule -> attributeRule.id().equals(attributeUuid))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Error while looking for an attribute rule!"));

            attributeDefinitionType.applyRule(newAttribute);
            attributeDefinitionType.removeRule(ruleToBeRemoved);
            instanceService.removeInstancesByType(type);
            return true;
        } catch (ExecutionException | InterruptedException e) {
            throw new InstanceOperationException(e);
        }
    }

    @Override
    public boolean updateAssociationDefinition(String uuid, String type, String associationUuid, String byRelation, String target) {
        try {
            TypeHandle typeHandle = future(registry.getType(type, uuid)).get().get();
            modicio.core.ModelElement associationDefinitionType = findTypeInHierarchyByAssociationDefinition(typeHandle.getModelElement(), associationUuid);
            AssociationRule ruleToBeRemoved = set(associationDefinitionType.definition().getAssociationRules())
                    .stream()
                    .filter(associationRule -> associationRule.id().equals(associationUuid))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Error while looking for an association rule!"));

            associationDefinitionType
                    .applyRule(AssociationRule.create(
                            byRelation,
                            target,
                            ruleToBeRemoved.multiplicity(),
                            ruleToBeRemoved.getInterface(),
                            Option.empty()));

            associationDefinitionType.removeRule(ruleToBeRemoved);
            instanceService.removeInstancesByType(type);
            return true;
        } catch (ExecutionException | InterruptedException e) {
            throw new InstanceOperationException(e);
        }

    }

    @Override
    public List<ModelElement> getAllModelElements() {
        try {
            List<ModelElement> modelElements = new ArrayList<>();
            set(future(registry.getReferences()).get()).forEach(typeHandle -> {
                modelElements.add(modelMapper.mapToModelElement(typeHandle.getModelElement()));
            });
            return modelElements;
        } catch (ExecutionException | InterruptedException e) {
            throw new InstanceOperationException(e);
        }
    }

    @Override
    public ModelElement getModelElementByIdAndName(String id, String name) {
        try {
            return modelMapper
                    .mapToModelElement(future(registry.getType(name, id))
                            .get()
                            .get()
                            .getModelElement());
        } catch (ExecutionException | InterruptedException e) {
            throw new InstanceOperationException(e);
        }
    }

    @Override
    public ModelElement addToOpenedModelElement(String from, String to, String currentPath) {
        try {
            String fromKey = from.contains(".") ? StringUtils.substringAfter(from, ".") : "";

            ModelElement modelElement = modelMapper
                    .mapToModelElement(
                            future(registry.getType(to, modicio.core.ModelElement.REFERENCE_IDENTITY()))
                                    .get()
                                    .get()
                                    .getModelElement()
                    );

            for (Attribute attribute : modelElement.getAttributes()) {
                String currentAttributeKey = attribute.getKey();
                if (StringUtils.isBlank(fromKey)) {
                    attribute.setKey(String.format("%s.%s", to, currentAttributeKey));
                } else {
                    attribute.setKey(String.format("%s.%s.%s", fromKey, to, currentAttributeKey));
                }
                attribute.setPath(String.format("%s.%s", currentPath, attribute.getPath()));
            }

            for (Association association : modelElement.getAssociations()) {
                association.setPath(String.format("%s.%s", currentPath, association.getPath()));
            }

            if (StringUtils.isNotBlank(fromKey)) {
                modelElement.setName(String.format("%s.%s", from, modelElement.getName()));
            } else {
                modelElement.setName(String.format("#.%s", modelElement.getName()));
            }

            return modelElement;
        } catch (ExecutionException | InterruptedException e) {
            throw new InstanceOperationException(e);
        }
    }

    @Override
    public Map<String, List<String>> getAllTypesAndTheirSupertypesMap() {
        try {
            Map<String, List<String>> allTypesAndSupertypesMap = new HashMap<>();
            for (String type : set(future(registry.getAllTypes()).get())) {
                TypeHandle typeHandle = future(registry.getType(type, modicio.core.ModelElement.REFERENCE_IDENTITY()))
                        .get()
                        .get();
                future(typeHandle.unfold()).get();
                if (!typeHandle.getIsTemplate()) {
                    allTypesAndSupertypesMap.put(type, new ArrayList<>(set(typeHandle.getModelElement().getTypeClosure())));
                }
            }
            return allTypesAndSupertypesMap;
        } catch (ExecutionException | InterruptedException e) {
            throw new InstanceOperationException(e);
        }
    }

    private modicio.core.ModelElement findTypeInHierarchyByAttributeDefinition(modicio.core.ModelElement modelElement, String attributeId) throws ExecutionException, InterruptedException {
        return findTypeInHierarchy(modelElement, attributeId, set(modelElement.definition().getAttributeRules()));
    }

    private modicio.core.ModelElement findTypeInHierarchyByAssociationDefinition(modicio.core.ModelElement modelElement, String associationId) throws ExecutionException, InterruptedException {
        return findTypeInHierarchy(modelElement, associationId, set(modelElement.definition().getAssociationRules()));
    }

    private modicio.core.ModelElement findTypeInHierarchy(modicio.core.ModelElement modelElement, String id, Set<? extends Rule> ruleSet) throws ExecutionException, InterruptedException {
        future(modelElement.unfold()).get();
        boolean isElementFound = ruleSet.stream().anyMatch(rule -> rule.id().equals(id));

        if (isElementFound) {
            return modelElement;
        } else {
            modicio.core.ModelElement type = null;
            for (modicio.core.ModelElement parentModelElement : set(modelElement.getParents())) {
                type = findTypeInHierarchyByAttributeDefinition(parentModelElement, id);
                if (type != null) {
                    break;
                }
            }
            return type;
        }
    }
}
