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

import ansk.development.domain.instance.InstanceElement;
import ansk.development.domain.instance.Link;
import ansk.development.domain.instance.Slot;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.ModelCom;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ansk.development.dsl.ShaclUtils.getXsdDatatypeByValue;

/**
 * Domain-specific model of a data graph.
 */
public class ShaclInstanceGraph extends ModelCom {

    private final static String CONSTRAINT_PREFIX = "eumcf";
    private final static String CONSTRAINT_NAMESPACE = "http://eumcf/shacl/";

    /**
     * Constructor.
     */
    public ShaclInstanceGraph() {
        super(org.apache.jena.graph.Factory.createGraphMem());
        this.setUpNamespace();
    }

    /**
     * Creates an instance by uuid and associates it with a class.
     *
     * @param instanceElement {@link InstanceElement}
     * @return {@link Resource}
     */
    public void createInstance(InstanceElement instanceElement) {
        super.createResource(CONSTRAINT_NAMESPACE + instanceElement.getUuid(),
                super.createResource(CONSTRAINT_NAMESPACE + instanceElement.getInstanceOf()));
        List<Slot> slots = instanceElement.getSlots();
        slots.forEach(slot -> addSlotToInstance(instanceElement.getUuid(), slot));
    }

    /**
     * Adds a slot key-value pair to instance.
     *
     * @param instanceUuid instance uuid
     * @param slot         {@link Slot}
     * @return {@link Resource}
     */
    private Resource addSlotToInstance(String instanceUuid, Slot slot) {
        return super.getResource(CONSTRAINT_NAMESPACE + instanceUuid)
                .addProperty(super.createProperty(CONSTRAINT_NAMESPACE + slot.getKey()), slot.getValue(), getXsdDatatypeByValue(slot.getValue()));
    }

    /**
     * Supplementary method for forAll() function.
     * SHACL does not support for_all semantics and must be provided with the exact number of required matching elements.
     *
     * @param instanceUuid uuid of an instance
     * @param navigation   navigation as list
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
     * @param link {@link Link}
     * @return {@link Resource}
     */
    public void addLinkToInstance(Link link) {
        if (doesInstanceExist(link.getTargetInstanceUuid())) {
            super
                    .getResource(CONSTRAINT_NAMESPACE + link.getInstanceUuid())
                    .addProperty(super.createProperty(CONSTRAINT_NAMESPACE + link.getName()),
                            super.createProperty(CONSTRAINT_NAMESPACE + link.getTargetInstanceUuid()));
        }
    }

    private boolean doesInstanceExist(String instanceUuid) {
        return super.containsResource(this.createResource(CONSTRAINT_NAMESPACE + instanceUuid));
    }

    private void setUpNamespace() {
        this.setNsPrefix(CONSTRAINT_PREFIX, CONSTRAINT_NAMESPACE);
    }
}
