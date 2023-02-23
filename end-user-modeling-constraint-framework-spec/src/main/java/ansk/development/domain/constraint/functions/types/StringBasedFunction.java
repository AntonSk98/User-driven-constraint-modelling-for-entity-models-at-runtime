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

import ansk.development.domain.ValidationUtils;
import ansk.development.domain.constraint.functions.ConstraintFunction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

/**
 * Specifies restrictions applied to attributes with a string representation.
 */
@Getter
public class StringBasedFunction extends ConstraintFunction {

    private final String attribute;
    private final Map<String, String> params;

    /**
     * Constructor.
     *
     * @param name      of a constraint
     * @param attribute of an attribute
     * @param params    params of a function
     */
    @JsonCreator
    public StringBasedFunction(
            @JsonProperty("name") String name,
            @JsonProperty("attribute") String attribute,
            @JsonProperty("params") Map<String, String> params) {
        super(name);
        ValidationUtils.validateAttribute(attribute);
        this.attribute = attribute;
        this.params = params;
    }

    public StringBasedFunction(
            String name,
            String attribute,
            Map<String, String> params,
            boolean asTemplate
    ) {
        super(name);
        if (!asTemplate) {
            ValidationUtils.validateAttribute(attribute);
        }
        this.attribute = attribute;
        this.params = params;
    }

    @Override
    public Optional<String> attribute() {
        return Optional.of(attribute);
    }


    @Override
    public Optional<Map<String, String>> params() {
        return Optional.ofNullable(params);
    }
}
