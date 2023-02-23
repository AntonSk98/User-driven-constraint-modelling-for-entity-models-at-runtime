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

package ansk.development.domain.template;

import ansk.development.domain.constraint.functions.ConstraintFunction;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Template for providing a {@link ConstraintFunction}.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectTemplatePlaceholder extends ConstraintFunction {

    private final String key;

    private final String value;

    public ObjectTemplatePlaceholder(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Optional<String> attribute() {
        throw new UnsupportedOperationException("This function must be used only internally");
    }

    @Override
    public Optional<String> navigation() {
        throw new UnsupportedOperationException("This function must be used only internally");
    }

    @Override
    public Optional<Map<String, String>> params() {
        throw new UnsupportedOperationException("This function must be used only internally");
    }

    @Override
    public Optional<List<ConstraintFunction>> booleanFunctions() {
        throw new UnsupportedOperationException("This function must be used only internally");
    }

    @Override
    public Optional<ConstraintFunction> lambdaFunction() {
        throw new UnsupportedOperationException("This function must be used only internally");
    }
}
