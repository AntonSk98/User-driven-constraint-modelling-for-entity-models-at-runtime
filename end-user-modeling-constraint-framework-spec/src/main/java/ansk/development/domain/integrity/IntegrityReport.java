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

package ansk.development.domain.integrity;

import ansk.development.domain.constraint.Constraint;
import lombok.Getter;

import java.util.Set;

/**
 * Integrity report.
 */
@Getter
public class IntegrityReport {
    private final String modelElement;

    private String oldPropertyName;
    private String newPropertyName;
    private String deletedPropertyName;
    private Set<Constraint> updatedConstraints;
    private Set<Constraint> deletedConstraints;

    /**
     * Constructor for integrity report as a result of updating the schema of a model element.
     *
     * @param modelElement       name of a model element
     * @param oldPropertyName    old property name
     * @param newPropertyName    new property name
     * @param updatedConstraints list of updated functions
     */
    public IntegrityReport(String modelElement, String oldPropertyName, String newPropertyName, Set<Constraint> updatedConstraints) {
        this.modelElement = modelElement;
        this.oldPropertyName = oldPropertyName;
        this.newPropertyName = newPropertyName;
        this.updatedConstraints = updatedConstraints;
    }

    /**
     * Constructor for integrity report as a result of deleting some elements from the schema of a model element.
     *
     * @param modelElement        name of a model element
     * @param deletedPropertyName deleted property name
     * @param deletedConstraints  list of invalid functions
     */
    public IntegrityReport(String modelElement, String deletedPropertyName, Set<Constraint> deletedConstraints) {
        this.modelElement = modelElement;
        this.deletedPropertyName = deletedPropertyName;
        this.deletedConstraints = deletedConstraints;
    }
}
