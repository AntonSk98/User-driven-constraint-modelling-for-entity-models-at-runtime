package de.antonsk98.development.domain.shacl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.ModelCom;
import org.apache.jena.shacl.vocabulary.SHACL;
import org.apache.jena.sparql.ARQConstants;
import org.apache.jena.vocabulary.RDFS;

/**
 * Extended {@link Model} with deep modelling support.
 *
 * @author Anton Skripin
 */
public class DeepModel extends ModelCom {

    public static final String DEEPMODEL = "http://deepmodel.de/";
    public static final String ATTRIBUTE_DATA = DEEPMODEL + "attribute_data/";
    public static final String ASSOCIATION_DATA = DEEPMODEL + "association_data/";

    /**
     * Constructor.
     */
    public DeepModel() {
        super(org.apache.jena.graph.Factory.createGraphMem());
        this.setUpNamespaces();
    }

    /**
     * Return shape constraint name by model name.
     *
     * @param modelName model name
     * @return name of the constraint shape
     */
    public static String getShapeName(String modelName) {
        return String.format("%sShape-%s", DEEPMODEL, modelName);
    }

    /**
     * Return shape target class by model name.
     *
     * @param modelName model name
     * @return name of the target class constraint
     */
    public static String getShapeTargetClass(String modelName) {
        return DEEPMODEL + modelName;
    }

    /**
     * Creates instance resource for a {@link DeepModel}.
     *
     * @param instanceId instance id
     * @param instanceOf instance of
     * @param identity   identity
     * @return new instance resource
     */
    public Resource createInstanceResource(String instanceId, String instanceOf, String identity) {
        return super.createResource(DEEPMODEL + instanceId, createClassResource(instanceOf, identity));
    }

    /**
     * Creates class resource for a {@link DeepModel}.
     *
     * @param instanceOf instance of
     * @param identity   identity
     * @return new class resource
     */
    public Resource createClassResource(String instanceOf, String identity) {
        return super.createResource(DEEPMODEL + instanceOf + "-" + identity);
    }

    /**
     * Creates a new resource for a target constraint.
     * @param targetConstraint target constraint
     * @return {@link  Resource}
     */
    public Resource createConstraintResource(String targetConstraint) {
        return super.createResource(DEEPMODEL + targetConstraint);
    }

    /**
     * Constructs an attribute property for a given attribute id.
     *
     * @param attributeId attribute id
     * @return attribute property
     */
    public Property createAttributeProperty(String attributeId) {
        return super.createProperty(ATTRIBUTE_DATA + attributeId);
    }

    /**
     * Constructs an association property for a given association id.
     *
     * @param associationId association id
     * @return association property
     */
    public Property createAssociationProperty(String associationId) {
        return super.createProperty(ASSOCIATION_DATA + associationId);
    }

    /**
     * Sets up default namespace for {@link DeepModel}.
     */
    private void setUpNamespaces() {
        super.setNsPrefix("deep_model", DeepModel.DEEPMODEL);
        super.setNsPrefix("attribute", DeepModel.ATTRIBUTE_DATA);
        super.setNsPrefix("association", DeepModel.ASSOCIATION_DATA);
        super.setNsPrefix("xsd", ARQConstants.xsdPrefix);
        super.setNsPrefix("rdfs", RDFS.uri);
        super.setNsPrefix("sh", SHACL.NS);
    }


}
