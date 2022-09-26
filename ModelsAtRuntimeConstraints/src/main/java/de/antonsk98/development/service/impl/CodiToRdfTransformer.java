package de.antonsk98.development.service.impl;

import de.antonsk98.development.domain.codi.CodiModel;
import de.antonsk98.development.domain.codi.deepInstance.*;
import de.antonsk98.development.domain.shacl.DeepModel;
import de.antonsk98.development.service.abs.Transformer;
import de.antonsk98.development.util.RdfUtils;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Transforms {@link CodiModel} to RDF model.
 *
 * @author Anton Skripin
 */
public class CodiToRdfTransformer implements Transformer<Model, CodiModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Model transform(CodiModel model) {
        Model m = ModelFactory.createDefaultModel();
        m.setNsPrefix("dm", DeepModel.DEEPMODEL);
        m.setNsPrefix("attribute", DeepModel.ATTRIBUTE_DATA);
        m.setNsPrefix("association", DeepModel.ASSOCIATION_DATA);
        m.setNsPrefix("xsd", XSDDatatype.XSD + "#");
        model.getDeepInstance().getClabjects().forEach(deepClabject -> {
            ClabjectData clabjectData = deepClabject.getClabjectData();
            Resource instanceResource = DeepModel.createInstanceResource(m, clabjectData.getInstanceId(), clabjectData.getInstanceOf());
            getDeepClabjectHierarchy(model.getDeepInstance(), deepClabject).forEach(clabject -> {
                addAttribute(m, instanceResource, clabject);
                addAssociation(m, instanceResource, clabject);
            });
        });
        return m;
    }

    /**
     * Adds attributes of a given clabject to RDF model.
     * @param m RDF model
     * @param instanceResource Resource representing a clabject in RDF
     * @param clabject Given clabject which attributes are constructed in RDF
     */
    private void addAttribute(Model m, Resource instanceResource, Clabject clabject) {
        Set<AttributeData> attributeDataSet = clabject.getAttributeData();
        attributeDataSet.forEach(attributeData -> instanceResource.addProperty(DeepModel.hasAttribute, DeepModel.createAttribute(m, String.format("%s-%s-%s",
                        clabject.getClabjectData().getInstanceOf(),
                        clabject.getClabjectData().getIdentity(),
                        attributeData.getKey()))
                .addProperty(DeepModel.attributeId, String.valueOf(attributeData.getId()))
                .addProperty(DeepModel.attributeInstanceId, attributeData.getInstanceId())
                .addProperty(DeepModel.attributeKey, attributeData.getKey())
                .addProperty(DeepModel.attributeValue, attributeData.getValue(), RdfUtils.getXsdDatatypeByValue(attributeData.getValue()))
                .addProperty(DeepModel.isFinalAttribute, Boolean.toString(attributeData.getIsFinal()))));
    }

    /**
     * Add associations of a given clabject to RDF model.
     * @param m RDF model
     * @param instanceResource Resource representing a clabject in RDF
     * @param clabject Given clabject which attributes are constructed in RDF
     */
    private void addAssociation(Model m, Resource instanceResource, Clabject clabject) {
        Set<AssociationData> associationDataSet = clabject.getAssociationData();
        associationDataSet.forEach(associationData -> instanceResource
                .addProperty(DeepModel.hasAssociation, DeepModel.createAssociation(m, String.format("%s-%s-%s",
                                clabject.getClabjectData().getInstanceOf(),
                                clabject.getClabjectData().getIdentity(),
                                associationData.getByRelation()))
                        .addProperty(DeepModel.associationId, associationData.getId().toString())
                        .addProperty(DeepModel.associationInstanceId, associationData.getInstanceId())
                        .addProperty(DeepModel.associationTargetInstanceId, associationData.getTargetInstanceId())
                        .addProperty(DeepModel.associationByRelation, associationData.getByRelation())
                        .addProperty(DeepModel.associationIsFinal, associationData.getIsFinal().toString())));
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
