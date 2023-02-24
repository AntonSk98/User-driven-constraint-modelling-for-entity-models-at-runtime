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

package ansk.development.domain.constraint.functions.types;

import ansk.development.domain.constraint.functions.ConstraintFunction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

/**
 * Enforces the evaluation engine to trigger the correspondence of an instance
 * to a constraint if and only if a given condition is satisfied.
 */
@Getter
public class ConditionalBasedFunction extends ConstraintFunction {

    private final List<ConstraintFunction> booleanFunctions;

    /**
     * Constructor.
     *
     * @param name            name of a constraint
     * @param nestedFunctions list of nested functions
     */
    @JsonCreator
    public ConditionalBasedFunction(
            @JsonProperty("name") String name,
            @JsonProperty("booleanFunctions") List<ConstraintFunction> nestedFunctions) {
        super(name);
        assert nestedFunctions != null && nestedFunctions.size() > 0;
        this.booleanFunctions = nestedFunctions;
    }

    @Override
    public Optional<List<ConstraintFunction>> booleanFunctions() {
        return Optional.of(booleanFunctions);
    }
}
