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

import ansk.development.domain.constraint.functions.ConstraintFunction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Defines a constraint definition.
 */
@Getter
@Setter
@NoArgsConstructor
public class Constraint {
    /**
     * Unique identifier
     */
    private String uuid;
    /**
     * Name of a constraint
     */
    private String name;
    /**
     * UUID of a model element a constraint is applied to
     */
    private String modelElementUuid;
    /**
     * Type a model element a constraint is applied to
     */
    private String modelElementType;
    /**
     * Message that is reported in case of a constraint violation
     */
    private String violationMessage;
    /**
     * Sets {@link ViolationLevel}.
     */
    private ViolationLevel violationLevel;
    /**
     * Every constraint has either one function or nested function with several constraints. See {@link ConstraintFunction}
     */
    private ConstraintFunction constraintFunction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constraint that = (Constraint) o;
        return Objects.equals(uuid, that.uuid) && Objects.equals(name, that.name) && Objects.equals(modelElementUuid, that.modelElementUuid) && Objects.equals(modelElementType, that.modelElementType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, modelElementUuid, modelElementType);
    }
}
