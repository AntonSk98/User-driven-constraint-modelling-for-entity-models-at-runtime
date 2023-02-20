package ansk.development.domain;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.ModelCom;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ansk.development.domain.ShaclUtils.getXsdDatatypeByValue;

/**
 * Domain-specific model of a data graph.
 */
public class ShaclConstraintData extends ModelCom {

    private final static String CONSTRAINT_PREFIX = "eumcf";
    private final static String CONSTRAINT_NAMESPACE = "http://eumcf/shacl/";

    /**
     * Constructor.
     */
    public ShaclConstraintData() {
        super(org.apache.jena.graph.Factory.createGraphMem());
        this.setUpNamespace();
    }

    /**
     * Creates an instance by uuid and associates it with a class.
     *
     * @param instanceUuid instance uuid
     * @param instanceOf   model element name an instance is instantiated from
     * @return {@link Resource}
     */
    public Resource createInstance(String instanceUuid, String instanceOf) {
        return super.createResource(CONSTRAINT_NAMESPACE + instanceUuid, super.createResource(CONSTRAINT_NAMESPACE + instanceOf));
    }

    /**
     * Adds a slot key-value pair to instance.
     *
     * @param instanceUuid instance uuid
     * @param key          key
     * @param value        value
     * @return {@link Resource}
     */
    public Resource addSlotToInstance(String instanceUuid, String key, String value) {
        return super.getResource(CONSTRAINT_NAMESPACE + instanceUuid).addProperty(super.createProperty(CONSTRAINT_NAMESPACE + key), value, getXsdDatatypeByValue(value));
    }

    /**
     * Supplementary method for forAll() function.
     * SHACL does not support for_all semantics and must be provided with the exact number of required matching elements.
     * @param instanceUuid uuid of an instance
     * @param navigation navigation as list
     * @return number of connected links with a target instance
     */
    public int getNumberOfConnectedLinks(String instanceUuid, List<String> navigation) {
        List<Property> properties = navigation.stream().map(path -> super.createProperty(CONSTRAINT_NAMESPACE + path)).collect(Collectors.toList());
        List<String> finalNodes = new ArrayList<>();
        finalNodes.add(CONSTRAINT_NAMESPACE + instanceUuid);
        properties.forEach(property -> {
            List<String> temporary = new ArrayList<>();
            finalNodes.forEach(node -> {
                Resource resource = super.getResource(node);
                temporary.addAll(resource.listProperties(property).toList().stream().map(statement -> statement.getObject().toString()).collect(Collectors.toList()));
            });
            finalNodes.clear();
            finalNodes.addAll(temporary);
        });
        return finalNodes.size();
    }

    /**
     * Adds a link between two instances.
     *
     * @param instanceUuid       instance uuid
     * @param byRelation         connector
     * @param targetInstanceUuid target instance uuid
     * @return {@link Resource}
     */
    public Resource addLinkToInstance(String instanceUuid, String byRelation, String targetInstanceUuid) {
        if (doesInstanceExist(targetInstanceUuid)) {
            return super.getResource(CONSTRAINT_NAMESPACE + instanceUuid).addProperty(super.createProperty(CONSTRAINT_NAMESPACE + byRelation), super.createProperty(CONSTRAINT_NAMESPACE + targetInstanceUuid));
        }
        return null;
    }

    private boolean doesInstanceExist(String instanceUuid) {
        return super.containsResource(this.createResource(CONSTRAINT_NAMESPACE + instanceUuid));
    }

    private void setUpNamespace() {
        this.setNsPrefix(CONSTRAINT_PREFIX, CONSTRAINT_NAMESPACE);
    }
}
