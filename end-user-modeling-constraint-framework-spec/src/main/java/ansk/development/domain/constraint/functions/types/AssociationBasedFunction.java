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

import java.util.Map;
import java.util.Optional;

/**
 * Association-based function.
 */
@Getter
public class AssociationBasedFunction extends ConstraintFunction {

    private final Map<String, String> params;

    /**
     * Constructor.
     *
     * @param name   of a constraint
     * @param params params of a function
     */
    @JsonCreator
    public AssociationBasedFunction(
            @JsonProperty("name") String name,
            @JsonProperty("params") Map<String, String> params) {
        super(name);
        this.params = params;
    }

    @Override
    public Optional<Map<String, String>> params() {
        return Optional.of(params);
    }
}
