package de.antonsk98.development.domain.shacl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 * RDF vocabulary for the deep model dialect.
 *
 * @author Anton Skripin
 */
public class DeepModel {

    public static final String DEEPMODEL = "http://deepmodel.de/";
    public static final String ATTRIBUTE_DATA = DEEPMODEL + "attribute_data/";
    public static final String ASSOCIATION_DATA = DEEPMODEL + "association_data/";

    private static final Model m = ModelFactory.createDefaultModel();

    public static Resource createInstanceResource(Model model, String instanceId, String instanceOf) {
        return model.createResource(DEEPMODEL + instanceId, m.createResource(DEEPMODEL + instanceOf));
    }

    public static final Property hasAttribute = m.createProperty(ATTRIBUTE_DATA + "hasAttribute");
    public static final Property attributeId = m.createProperty(ATTRIBUTE_DATA + "id");
    public static final Property attributeInstanceId = m.createProperty(ATTRIBUTE_DATA + "instanceId");
    public static final Property attributeKey = m.createProperty(ATTRIBUTE_DATA + "key");
    public static final Property attributeValue = m.createProperty(ATTRIBUTE_DATA + "value");
    public static final Property isFinalAttribute = m.createProperty(ATTRIBUTE_DATA + "isFinal");

    public static Resource createAttribute(Model m, String attributeId) {
        return m.createResource(ATTRIBUTE_DATA + attributeId);
    }


    public static final Property hasAssociation = m.createProperty(ASSOCIATION_DATA + "hasAssociation");
    public static final Property associationId = m.createProperty(ASSOCIATION_DATA + "id");
    public static final Property associationByRelation = m.createProperty(ASSOCIATION_DATA + "byRelation");
    public static final Property associationInstanceId = m.createProperty(ASSOCIATION_DATA + "instanceId");
    public static final Property associationTargetInstanceId = m.createProperty(ASSOCIATION_DATA + "targetInstanceId");
    public static final Property associationIsFinal = m.createProperty(ASSOCIATION_DATA + "isFinal");

    public static Resource createAssociation(Model m, String associationId) {
        return m.createResource(ASSOCIATION_DATA + associationId);
    }


}
