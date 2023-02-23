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

package ansk.development.eumcf.service.api;

import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.instance.InstanceElement;
import ansk.development.domain.instance.Link;
import ansk.development.domain.instance.Slot;
import ansk.development.service.api.ConstraintValidationService;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface InstanceService {

    /**
     * Removes all instances from a registry by a given type.
     *
     * @param type of a model element
     * @return true if instances are removed, false otherwise
     */
    boolean removeInstancesByType(String type);

    /**
     * Removes an instance from a registry by a given uuid.
     *
     * @param uuid of an instance
     * @return true if an instance is removed, false otherwise
     */
    boolean deleteInstanceById(String uuid);

    /**
     * Creates an instance.
     *
     * @param instanceOf type of model element
     * @param slots      list of instantiated attributes
     * @param links      list of instantiated associations
     * @return true if an instance is created
     */
    boolean createInstance(String instanceOf, List<Slot> slots, List<Link> links);

    /**
     * Updates an instance
     *
     * @param uuid  of an instance
     * @param slots list of instantiated attributes
     * @param links list of instantiated associations
     * @return true if an instance is updated
     */
    boolean updateInstance(String uuid, List<Slot> slots, List<Link> links);

    /**
     * Returns an instance by uuid.
     *
     * @param uuid of an instance
     * @return {@link InstanceElement}
     */
    InstanceElement getInstanceByUuid(String uuid);

    /**
     * Return all instances for each type.
     * Example: "[Car: [Honda, BMW, Audi], Engineer: [PersonA, PersonB, PersonC]]"
     *
     * @return map
     */
    Map<String, List<InstanceElement>> getTypeToInstancesMap();

    /**
     * Enriches an {@link InstanceElement} with all associations that are not yet instantiated by links.
     *
     * @param instanceElement to be enriched
     * @return {@link InstanceElement}
     */
    InstanceElement withAssociations(InstanceElement instanceElement);

    /**
     * Returns a required instance graph based on required subgraph elements.
     * It requires a set of all necessary model element types for every constraint function defined under one constraint.
     * Example: Constraint: {constraintFunctionA: this, SoftwareEngineer, Project; constraintFunctionB: this, Sprint}.
     * The resulting structure of an input is the following:
     * [[SoftwareEngineer, Project],[Sprint]]
     * Hence, before checking the constraint upon the instantiation of 'this', the function does the following:
     * 1) get all associated instances of this with SoftwareEngineer -> get all associated instances of SoftwareEngineer, that is associated with this, with Spring
     * 2) get all associated instances of this with Spring
     * As a result the necessary instance graph will be returned.
     *
     * @param instanceUuid             uuid of an instance for constraint validation
     * @param requiredSubgraphElements See {@link ConstraintValidationService#getRequiredSubgraphElements(Constraint)}
     * @return subgraph needed for constraint evaluation
     */
    List<InstanceElement> getRequiredSubgraph(String instanceUuid, Set<Set<String>> requiredSubgraphElements);
}
