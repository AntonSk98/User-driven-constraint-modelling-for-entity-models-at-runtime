package de.antonsk98.development.service.impl;

import de.antonsk98.development.domain.codi.CodiModel;
import de.antonsk98.development.domain.codi.deepInstance.*;
import de.antonsk98.development.domain.codi.model.ModelElement;
import de.antonsk98.development.domain.shacl.DeepModel;
import de.antonsk98.development.service.api.Transformer;
import de.antonsk98.development.util.RdfUtils;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDFS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Transforms {@link CodiModel} to RDF model.
 *
 * @author Anton Skripin
 */
public class CodiToDataModelTransformer implements Transformer<Model, CodiModel> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Model transform(CodiModel model) {
        DeepModel deepModel = new DeepModel();
        model.getDeepInstance().getClabjects().forEach(deepClabject -> {
            constructClabjectHierarchy(deepModel, model.getDeepInstance(), deepClabject);
            ClabjectData clabjectData = deepClabject.getClabjectData();
            Resource instanceResource = deepModel.createInstanceResource( clabjectData.getInstanceId(), clabjectData.getInstanceOf(), clabjectData.getIdentity());
            getDeepClabjectHierarchy(model.getDeepInstance(), deepClabject).forEach(clabject -> {
                addAttribute(deepModel, instanceResource, clabject);
                addAssociation(deepModel, instanceResource, clabject, model.getModel().getModelElement());
            });
        });
        return deepModel;
    }

    /**
     * Constructs rdfs child-of triples for each clabject being present in the deep instance hierarchy.
     *
     * @param deepModel            RDF model
     * @param deepInstance Deep instance hierarchy
     * @param clabject     Given clabject for which the child-of relationship is constructed
     */
    private void constructClabjectHierarchy(DeepModel deepModel, DeepInstance deepInstance, Clabject clabject) {
        String childInstanceOf = clabject.getClabjectData().getInstanceOf();
        String childIdentity = clabject.getClabjectData().getIdentity();
        clabject.getExtensionData().stream().map(ExtensionData::getParentInstanceId).collect(Collectors.toList())
                .forEach(instanceId -> {
                    deepInstance
                            .getClabjects()
                            .stream()
                            .map(Clabject::getClabjectData)
                            .filter(clabjectData -> clabjectData.getInstanceId().equals(instanceId))
                            .forEach(clabjectData -> deepModel.add(
                                    deepModel.createClassResource(childInstanceOf, childIdentity),
                                    RDFS.subClassOf,
                                    deepModel.createClassResource(clabjectData.getInstanceOf(), clabjectData.getIdentity())));
                });
    }

    /**
     * Adds attributes of a given clabject to RDF model.
     *
     * @param deepModel                RDF model
     * @param instanceResource Resource representing a clabject in RDF
     * @param clabject         Given clabject which attributes are constructed in RDF
     */
    private void addAttribute(DeepModel deepModel, Resource instanceResource, Clabject clabject) {
        Set<AttributeData> attributeDataSet = clabject.getAttributeData();
        attributeDataSet.forEach(attributeData -> instanceResource.addProperty(deepModel
                .createAttributeProperty(
                        String.format("%s-%s-%s",
                                clabject.getClabjectData().getInstanceOf(),
                                clabject.getClabjectData().getIdentity(),
                                attributeData.getKey())), attributeData.getValue(), RdfUtils.getXsdDatatypeByValue(attributeData.getValue())));
    }

    /**
     * Add associations of a given clabject to RDF model.
     *
     * @param deepModel                RDF model
     * @param instanceResource Resource representing a clabject in RDF
     * @param clabject         Given clabject which attributes are constructed in RDF
     * @param modelElement     Model element
     */
    private void addAssociation(DeepModel deepModel, Resource instanceResource, Clabject clabject, Set<ModelElement> modelElement) {
        Set<AssociationData> associationDataSet = clabject.getAssociationData();
        associationDataSet.forEach(associationData -> {
            getTargetModelType(modelElement, associationData)
                    .forEach(targetModeType -> instanceResource
                            .addProperty(deepModel.createAssociationProperty(
                                    String.format("%s-%s-%s",
                                            clabject.getClabjectData().getInstanceOf(),
                                            clabject.getClabjectData().getIdentity(),
                                            associationData.getByRelation())),
                                    deepModel.createResource(
                                            deepModel.createClassResource("Todo", "854c2eaf-5201-458a-aaf8-273ba23e3cc5"))));
        });
    }

    private List<String> getTargetModelType(Set<ModelElement> modelElementSet, AssociationData associationData) {
        List<String> targetModelTypes = new ArrayList<>();
            String byRelation = associationData.getByRelation();
            modelElementSet.forEach(modelElement -> {
                Set<String> associationsSet = modelElement.getAssociations();
                associationsSet
                        .stream()
                        .filter(associationString -> associationString.contains(byRelation))
                        .findFirst()
                        .ifPresent(s -> {
                            targetModelTypes.add(s.split(":")[2]);
                        });
            });
        return targetModelTypes;
    }

    /**
     * Recursive operation that construct a clabject hierarchy up to its top-level element.
     *
     * @param deepInstance deep instance
     * @param clabject     current clabject of a deep instance hierarchy
     * @return clabject hierarchy for a given clabject
     */
    public List<Clabject> getDeepClabjectHierarchy(DeepInstance deepInstance, Clabject clabject) {
        List<Clabject> deepClabjects = new ArrayList<>(List.of(clabject));
        performOnParentClabject(
                deepInstance,
                clabject,
                (parentClabject) -> deepClabjects.addAll(getDeepClabjectHierarchy(deepInstance, parentClabject)));
        return deepClabjects;
    }

    /**
     * Finds a parent clabject of the clabject and applies the given operation on the parent clabject.
     *
     * @param deepInstance deep instance
     * @param clabject     current clabject of a deep instance hierarchy
     * @param consumer     operation to be performed upon a parent clabject
     */
    public void performOnParentClabject(DeepInstance deepInstance, Clabject clabject, Consumer<Clabject> consumer) {
        clabject.getExtensionData().forEach(extensionData -> {
            String parentInstanceId = extensionData.getParentInstanceId();
            Optional<Clabject> instanceOptional = deepInstance
                    .getClabjects()
                    .stream()
                    .filter(instance -> instance.getClabjectData().getInstanceId().equals(parentInstanceId))
                    .findFirst();
            instanceOptional.ifPresent(consumer);
        });
    }
}
