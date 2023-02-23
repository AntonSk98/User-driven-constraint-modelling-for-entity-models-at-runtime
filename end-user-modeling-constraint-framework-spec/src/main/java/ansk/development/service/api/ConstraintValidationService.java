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

package ansk.development.service.api;

import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.ConstraintValidationReport;
import ansk.development.domain.instance.InstanceElement;

import java.util.List;
import java.util.Set;

/**
 * Provides classes needed to validate a constraint.
 */
public interface ConstraintValidationService {

    /**
     * Returns necessary elements that must be present in an instance graph to evaluate a constraint.
     *
     * @param constraint is used to analyze required depth and element of an instance graph
     * @return required subgraph elements
     */

    Set<Set<String>> getRequiredSubgraphElements(Constraint constraint);

    /**
     * Validates a constraint.
     *
     * @param uuid                  of an element that should be evaluated against constraint conformance
     * @param subgraphForValidation subgraph of instance data and relations
     * @param constraint            {@link Constraint}
     * @return validation report
     */
    ConstraintValidationReport validateConstraint(String uuid, List<InstanceElement> subgraphForValidation, Constraint constraint);
}
