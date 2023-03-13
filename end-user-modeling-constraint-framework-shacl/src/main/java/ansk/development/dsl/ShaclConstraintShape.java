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

package ansk.development.dsl;

import ansk.development.exception.constraint.GraphConstraintException;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.ModelCom;
import org.topbraid.shacl.vocabulary.SH;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static ansk.development.dsl.ShaclUtils.getXsdDatatypeByValue;

/**
 * Domain class to define constraints using SHACL.
 */
public class ShaclConstraintShape extends ModelCom {

    private final static String CONSTRAINT_PREFIX = "eumcf";
    private final static String CONSTRAINT_NAMESPACE = "http://eumcf/shacl/";
    private final static String SHACL_PREFIX = "sh";
    private final static String SHACL_NAMESPACE = "http://www.w3.org/ns/shacl#";

    private final static String TARGET_INSTANCE_PREFIX = "instanceShape";
    private final static String TARGET_INSTANCE_NAMESPACE = "http://eumcf/shacl/shape/";

    private String targetInstanceResource;
    private String targetInstance;

    /**
     * Constructor.
     */
    public ShaclConstraintShape() {
        super(org.apache.jena.graph.Factory.createGraphMem());
        this.setUpNamespace();
    }

    public String loadTargetInstance() {
        return targetInstance;
    }

    public Resource getConstraint() {
        return this.getResource(targetInstanceResource);
    }

    public void loadTargetInstanceByCondition(String instanceUuid, Supplier<Boolean> condition) {
        if (condition.get()) {
            this.loadTargetInstance(instanceUuid);
        }
    }

    public void loadTargetInstance(String instanceUuid) {
        targetInstance = instanceUuid;
        targetInstanceResource = TARGET_INSTANCE_NAMESPACE + instanceUuid;
        Resource context = this.createResource(TARGET_INSTANCE_NAMESPACE + instanceUuid, SH.NodeShape);
        context.addProperty(SH.targetNode, this.createResource(CONSTRAINT_NAMESPACE + instanceUuid));
    }

    public Resource minCardinality(String outType, int minValue) {
        return this.getResource(targetInstanceResource).addProperty(SH.property, this.createResource()
                .addProperty(SH.path, this.createProperty(CONSTRAINT_NAMESPACE + outType))
                .addProperty(SH.minCount, String.valueOf(minValue), XSDDatatype.XSDint));
    }

    public Resource maxCardinality(String outType, int maxValue) {
        return this.getResource(targetInstanceResource).addProperty(SH.property, this.createResource()
                .addProperty(SH.path, this.createProperty(CONSTRAINT_NAMESPACE + outType))
                .addProperty(SH.maxCount, String.valueOf(maxValue), XSDDatatype.XSDint));
    }

    public Resource notNullOrEmpty(String attribute, boolean nested) {
        Resource resource = getResource(nested);
        return resource.addProperty(SH.property, this.createResource()
                .addProperty(SH.path, this.createProperty(CONSTRAINT_NAMESPACE + attribute))
                .addProperty(SH.minCount, "1", XSDDatatype.XSDint)
                .addProperty(SH.minLength, "1", XSDDatatype.XSDint));
    }

    public Resource unique(String attribute) {
        throw new GraphConstraintException("This function cannot be implemented using the standard syntax of SHACL!");
    }

    public Resource or(List<Resource> nestedFunctions, boolean nested) {
        Resource resource = getResource(nested);
        return resource.addProperty(SH.or, this.createList(nestedFunctions.iterator()));
    }

    public Resource and(List<Resource> nestedFunctions, boolean nested) {
        Resource resource = getResource(nested);
        return resource.addProperty(SH.and, this.createList(nestedFunctions.iterator()));
    }

    public Resource equals(String attribute, String value, boolean nested) {
        Resource resource = getResource(nested);
        return resource.addProperty(SH.property, this.createResource()
                .addProperty(SH.path, this.createProperty(CONSTRAINT_NAMESPACE + attribute))
                .addProperty(SH.pattern, value));
    }

    public Resource lessThanOrEquals(String attribute, String value, boolean nested) {
        Resource resource = getResource(nested);
        return resource.addProperty(SH.property, this.createResource()
                .addProperty(SH.path, this.createProperty(CONSTRAINT_NAMESPACE + attribute))
                .addProperty(SH.maxInclusive, value, getXsdDatatypeByValue(value)));
    }

    public Resource lessThan(String attribute, String value, boolean nested) {
        Resource resource = getResource(nested);
        return resource.addProperty(SH.property, this.createResource()
                .addProperty(SH.path, this.createProperty(CONSTRAINT_NAMESPACE + attribute))
                .addProperty(SH.maxExclusive, value, getXsdDatatypeByValue(value)));
    }

    public Resource greaterThanOrEquals(String attribute, String value, boolean nested) {
        Resource resource = getResource(nested);
        return resource.addProperty(SH.property, this.createResource()
                .addProperty(SH.path, this.createProperty(CONSTRAINT_NAMESPACE + attribute))
                .addProperty(SH.minInclusive, value, getXsdDatatypeByValue(value)));
    }

    public Resource greaterThan(String attribute, String value, boolean nested) {
        Resource resource = getResource(nested);
        return resource.addProperty(SH.property, this.createResource()
                .addProperty(SH.path, this.createProperty(CONSTRAINT_NAMESPACE + attribute))
                .addProperty(SH.minExclusive, value, getXsdDatatypeByValue(value)));
    }

    public Resource maxLength(String attribute, String length, boolean nested) {
        Resource resource = getResource(nested);
        return resource.addProperty(SH.property, this.createResource()
                .addProperty(SH.path, this.createProperty(CONSTRAINT_NAMESPACE + attribute))
                .addProperty(SH.maxLength, length, XSDDatatype.XSDint));
    }

    public Resource forAll(List<String> navigation, Resource lambdaFunction, int totalLinkNumber, boolean nested) {
        Resource resource = getResource(nested);
        List<Property> properties = navigation.stream().map(path -> this.createProperty(CONSTRAINT_NAMESPACE + path)).collect(Collectors.toList());
        return resource.addProperty(SH.property, this.createResource()
                .addProperty(SH.path, this.createList(properties.iterator()))
                .addProperty(SH.qualifiedValueShape, lambdaFunction)
                .addProperty(SH.qualifiedMinCount, String.valueOf(totalLinkNumber), XSDDatatype.XSDint));
    }

    public Resource minLength(String attribute, String length, boolean nested) {
        Resource resource = getResource(nested);
        return resource.addProperty(SH.property, this.createResource()
                .addProperty(SH.path, this.createProperty(CONSTRAINT_NAMESPACE + attribute))
                .addProperty(SH.minLength, length, XSDDatatype.XSDint));
    }

    public Resource forSome(List<String> navigation, Resource lambdaFunction, boolean nested) {
        Resource resource = getResource(nested);
        List<Property> properties = navigation.stream().map(path -> this.createProperty(CONSTRAINT_NAMESPACE + path)).collect(Collectors.toList());
        return resource.addProperty(SH.property, this.createResource()
                .addProperty(SH.path, this.createList(properties.iterator()))
                .addProperty(SH.qualifiedValueShape, lambdaFunction)
                .addProperty(SH.qualifiedMinCount, String.valueOf(1), XSDDatatype.XSDint));
    }

    public Resource forExactly(List<String> navigation, Resource lambdaFunction, int matches, boolean nested) {
        Resource resource = getResource(nested);
        List<Property> properties = navigation.stream().map(path -> this.createProperty(CONSTRAINT_NAMESPACE + path)).collect(Collectors.toList());
        return resource.addProperty(SH.property, this.createResource()
                .addProperty(SH.path, this.createList(properties.iterator()))
                .addProperty(SH.qualifiedValueShape, lambdaFunction)
                .addProperty(SH.qualifiedMinCount, String.valueOf(matches), XSDDatatype.XSDint));
    }

    public Resource forNone(List<String> navigation, Resource lambdaFunction, boolean nested) {
        Resource resource = getResource(nested);
        List<Property> properties = navigation.stream().map(path -> this.createProperty(CONSTRAINT_NAMESPACE + path)).collect(Collectors.toList());
        return resource.addProperty(SH.property, this.createResource()
                .addProperty(SH.path, this.createList(properties.iterator()))
                .addProperty(SH.qualifiedValueShape, lambdaFunction)
                .addProperty(SH.qualifiedMaxCount, String.valueOf(0), XSDDatatype.XSDint));
    }

    public Resource runtimeFunction(String runtimeFunction) {
        return this.createResource(runtimeFunction);
    }

    private Resource getResource(boolean nested) {
        return nested ? this.createResource() : this.getResource(targetInstanceResource);
    }

    private void setUpNamespace() {
        this.setNsPrefix(TARGET_INSTANCE_PREFIX, TARGET_INSTANCE_NAMESPACE);
        this.setNsPrefix(SHACL_PREFIX, SHACL_NAMESPACE);
        this.setNsPrefix(CONSTRAINT_PREFIX, CONSTRAINT_NAMESPACE);
    }
}
