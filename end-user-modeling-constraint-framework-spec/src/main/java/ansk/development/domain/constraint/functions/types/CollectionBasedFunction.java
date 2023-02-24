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
 * Defines an operation that must be performed on a collection with every traversal step satisfying a true condition.
 */
@Getter
public class CollectionBasedFunction extends ConstraintFunction {

    private final String navigation;

    private final ConstraintFunction lambdaFunction;

    private final Map<String, String> params;


    /**
     * Name of a function
     *
     * @param name           name
     * @param navigation     path to a collection attribute relatively to the context path
     * @param lambdaFunction {@link ConstraintFunction}
     */
    @JsonCreator
    public CollectionBasedFunction(
            @JsonProperty("name") String name,
            @JsonProperty("navigation") String navigation,
            @JsonProperty("lambdaFunction") ConstraintFunction lambdaFunction,
            @JsonProperty("params") Map<String, String> params) {
        super(name);
        ValidationUtils.validateNavigation(navigation);
        ValidationUtils.validateLambdaFunctionHasNoNavigation(name, lambdaFunction);
        ValidationUtils.validateLambdaFunctionIsNotLogicalFunction(name, lambdaFunction);
        this.navigation = navigation;
        this.lambdaFunction = lambdaFunction;
        this.params = params;
    }

    public CollectionBasedFunction(
            String name,
            String navigation,
            ConstraintFunction constraintFunction,
            Map<String, String> params,
            boolean asTemplate) {
        super(name);
        if (!asTemplate) {
            ValidationUtils.validateNavigation(navigation);
            ValidationUtils.validateLambdaFunctionHasNoNavigation(name, constraintFunction);
            ValidationUtils.validateLambdaFunctionIsNotLogicalFunction(name, constraintFunction);
        }
        this.navigation = navigation;
        this.lambdaFunction = constraintFunction;
        this.params = params;
    }

    @Override
    public Optional<String> navigation() {
        return Optional.of(navigation);
    }

    @Override
    public Optional<Map<String, String>> params() {
        return Optional.ofNullable(params);
    }

    @Override
    public Optional<ConstraintFunction> lambdaFunction() {
        return Optional.of(lambdaFunction);
    }
}
