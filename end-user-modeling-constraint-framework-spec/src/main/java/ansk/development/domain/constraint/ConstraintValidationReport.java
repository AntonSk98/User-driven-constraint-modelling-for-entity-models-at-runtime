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

package ansk.development.domain.constraint;

import lombok.Getter;

/**
 * POJO that is returned as a result of a constraint violation.
 */
@Getter
public class ConstraintValidationReport {

    private final String uuid;
    private final String name;
    private final String elementType;
    private final ValidationResult result;
    private final boolean isValid;
    private final String violationMessage;

    /**
     * Constructor
     *
     * @param uuid             uuid of an instance that is used as a context for constraint validation
     * @param name             of a constraint
     * @param elementType      context element which a constraint is applied to
     * @param isValid          whether evaluation result is true
     * @param violationLevel   See {@link ViolationLevel}
     * @param violationMessage Message that should be returned to an end-user in case a constraint is invalid
     */
    public ConstraintValidationReport(String uuid, String name, String elementType, boolean isValid, ViolationLevel violationLevel, String violationMessage) {
        this.uuid = uuid;
        this.name = name;
        this.elementType = elementType;
        this.isValid = isValid;
        this.result = resolveResult(isValid, violationLevel);
        this.violationMessage = violationMessage;
    }

    private static ValidationResult resolveResult(boolean isValid, ViolationLevel result) {
        if (isValid) {
            return ValidationResult.VALID;
        }
        if (ViolationLevel.WARN.equals(result)) {
            return ValidationResult.WARN;
        }
        if (ViolationLevel.ERROR.equals(result)) {
            return ValidationResult.INVALID;
        }
        throw new RuntimeException("Unknown violation level: " + result);
    }
}
