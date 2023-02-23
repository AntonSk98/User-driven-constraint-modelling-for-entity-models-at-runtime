/*
 * Copyright (c) 2023 Anton Skripin
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package ansk.development.eumcf.service;

import ansk.development.domain.model.Association;
import ansk.development.domain.model.Attribute;
import ansk.development.domain.model.ModelElement;
import ansk.development.eumcf.service.api.InstanceService;
import ansk.development.eumcf.service.api.ModelService;
import ansk.development.exception.model.ModelOperationException;
import ansk.development.mapper.ModelMapper;
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

import static ansk.development.eumcf.util.ScalaToJavaMapper.future;
import static ansk.development.eumcf.util.ScalaToJavaMapper.set;

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
            throw new ModelOperationException(e);
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
            throw new ModelOperationException(e);
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
            throw new ModelOperationException(e);
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
            throw new ModelOperationException(e);
        }
    }

    @Override
    public ModelElement addToOpenedModelElement(String from, String to, String currentNavigation) {
        try {
            String fromKey = from.contains("->") ? StringUtils.substringAfter(from, "->") : "";

            ModelElement toModelElement = modelMapper
                    .mapToModelElement(
                            future(registry.getType(to, modicio.core.ModelElement.REFERENCE_IDENTITY()))
                                    .get()
                                    .get()
                                    .getModelElement()
                    );

            for (Attribute attribute : toModelElement.getAttributes()) {
                String currentAttributeKey = attribute.getKey();
                if (StringUtils.isBlank(fromKey)) {
                    attribute.setKey(String.format("#->%s.%s", to, currentAttributeKey));
                } else {
                    attribute.setKey(String.format("#->%s->%s.%s", fromKey, to, currentAttributeKey));
                }
                attribute.setNavigation(currentNavigation);
            }

            for (Association association : toModelElement.getAssociations()) {
                association.setNavigation(String.format("%s.%s", currentNavigation, association.getNavigation()));
            }

            if (StringUtils.isBlank(fromKey)) {
                toModelElement.setName(String.format("#->%s", toModelElement.getName()));
            } else {
                toModelElement.setName(String.format("#->%s->%s", fromKey, toModelElement.getName()));
            }

            return toModelElement;
        } catch (ExecutionException | InterruptedException e) {
            throw new ModelOperationException(e);
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
            throw new ModelOperationException(e);
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
