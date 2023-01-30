package anton.skripin.development.eumcf.modicio_space.service;

import anton.skripin.development.domain.instance.Link;
import anton.skripin.development.domain.instance.Slot;
import lombok.SneakyThrows;
import modicio.core.*;
import modicio.core.rules.AssociationRule;
import modicio.core.rules.AttributeRule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import scala.Option;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static anton.skripin.development.eumcf.util.ScalaToJavaMapping.future;
import static anton.skripin.development.eumcf.util.ScalaToJavaMapping.set;

@Service
public class ModicioService {

    private final Registry registry;

    public ModicioService(Registry registry) {
        this.registry = registry;
    }

    @SneakyThrows
    public void updateAttributeDefinition(String id, String type, String attributeId, String key, String datatype) {
        AttributeRule newAttribute = AttributeRule.create(key, datatype, false, Option.empty());

        TypeHandle typeHandle = future(registry.getType(type, id)).get().get();


        ModelElement attributeDefinitionType = findTypeInHierarchyByAttributeDefinition(typeHandle.getModelElement(), attributeId);
        AttributeRule ruleToBeRemoved = set(attributeDefinitionType.deepAttributeRuleSet())
                .stream()
                .filter(attributeRule -> attributeRule.id().equals(attributeId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Error while looking for an attribute rule!"));

        attributeDefinitionType.applyRule(newAttribute);
        attributeDefinitionType.removeRule(ruleToBeRemoved);
        removeAffectedInstances(type);
    }

    @SneakyThrows
    public void updateAssociationDefinition(String id, String type, String associationId, String byRelation, String target) {
        TypeHandle typeHandle = future(registry.getType(type, id)).get().get();
        ModelElement associationDefinitionType = findTypeInHierarchyByAssociationDefinition(typeHandle.getModelElement(), associationId);
        AssociationRule ruleToBeRemoved = set(associationDefinitionType.definition().getAssociationRules())
                .stream().filter(associationRule -> associationRule.id().equals(associationId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Error while looking for an association rule!"));

        associationDefinitionType.applyRule(AssociationRule.create(byRelation, target, ruleToBeRemoved.multiplicity(), ruleToBeRemoved.getInterface(), Option.empty()));
        associationDefinitionType.removeRule(ruleToBeRemoved);
        removeAffectedInstances(type);
    }

    @SneakyThrows
    private void removeAffectedInstances(String type) {
        set(future(registry.getAll(type))
                .get())
                .stream()
                .map(DeepInstance::instanceId)
                .forEach(registry::autoRemove);
    }

    private ModelElement findTypeInHierarchyByAttributeDefinition(ModelElement modelElement, String attributeId) throws ExecutionException, InterruptedException {
        return findTypeInHierarchy(modelElement, attributeId, set(modelElement.definition().getAttributeRules()));
    }

    private ModelElement findTypeInHierarchyByAssociationDefinition(ModelElement modelElement, String associationId) throws ExecutionException, InterruptedException {
        return findTypeInHierarchy(modelElement, associationId, set(modelElement.definition().getAssociationRules()));
    }

    private ModelElement findTypeInHierarchy(ModelElement modelElement, String id, Set<? extends Rule> ruleSet) throws ExecutionException, InterruptedException {
        future(modelElement.unfold()).get();
        boolean isElementFound = ruleSet.stream().anyMatch(rule -> rule.id().equals(id));

        if (isElementFound) {
            return modelElement;
        } else {
            ModelElement type = null;
            for (ModelElement parentModelElement : set(modelElement.getParents())) {
                type = findTypeInHierarchyByAttributeDefinition(parentModelElement, id);
                if (type != null) {
                    break;
                }
            }
            return type;
        }
    }

    @SneakyThrows
    public void deleteInstanceById(String uuid) {
        future(registry.autoRemove(uuid)).get();
    }

    @SneakyThrows
    public void createInstance(String instanceOf, List<Slot> slots, List<Link> links) {
        DeepInstance deepInstance = future(registry.instanceFactory().newInstance(instanceOf)).get();
        commonOperation(deepInstance, slots, links);
    }

    @SneakyThrows
    public void updateInstance(String uuid, List<Slot> slots, List<Link> links) {
        DeepInstance toBeUpdated = future(registry.get(uuid)).get().get();
        set(toBeUpdated.getDeepAssociations()).forEach(associationData -> toBeUpdated.removeAssociation(associationData.id()));
        commonOperation(toBeUpdated, slots, links);
    }

    private void commonOperation(DeepInstance deepInstance, List<Slot> slots, List<Link> links) throws ExecutionException, InterruptedException {
        future(deepInstance.unfold()).get();

        for (Slot slot: slots) {
            deepInstance.assignDeepValue(slot.getKey(), slot.getValue());
        }

        for (Link link: links) {
            if (StringUtils.isBlank(link.getTargetInstanceUuid())) {
                continue;
            }
            DeepInstance to = future(registry.get(link.getTargetInstanceUuid().trim())).get().get();
            future(to.unfold()).get();
            deepInstance.associate(to, to.typeHandle().getTypeName(), link.getName());
        }
    }
}
