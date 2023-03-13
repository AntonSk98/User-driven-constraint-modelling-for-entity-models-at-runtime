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

package ansk.development.domain;

import ansk.development.dsl.ConstraintGraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class GremlinConstraint {

    private ConstraintGraphTraversal<?, ?> context;

    private List<String> navigation;

    private GraphTraversal<?, Boolean> constraintFunction;

    private GraphTraversal<?, Boolean> lambdaFunction;

    private Map<String, String> params;

    private String attribute;

    private String runtimeFunction;

    private List<GraphTraversal<?, Boolean>> nestedFunctions;


    public Optional<ConstraintGraphTraversal<?, ?>> context() {
        return Optional.ofNullable(context);
    }

    public Optional<List<String>> navigation() {
        return Optional.ofNullable(navigation);
    }

    public Optional<GraphTraversal<?, Boolean>> lambdaFunction() {
        return Optional.ofNullable(lambdaFunction);
    }

    public Optional<Map<String, String>> params() {
        return Optional.ofNullable(params);
    }

    public Optional<String> attribute() {
        return Optional.ofNullable(attribute);
    }

    public Optional<String> runtimeFunction() {
        return Optional.ofNullable(runtimeFunction);
    }

    public Optional<List<GraphTraversal<?, Boolean>>> nestedFunctions() {
        return Optional.ofNullable(nestedFunctions);
    }

    public void setContext(ConstraintGraphTraversal<?, ?> context) {
        this.context = context;
    }

    public void setContextByCondition(ConstraintGraphTraversal<?, ?> context, Supplier<Boolean> condition) {
        if (condition.get()) {
            this.context = context;
        }
    }

    public void setNavigation(List<String> navigation) {
        this.navigation = navigation;
    }

    public void setLambdaFunction(GraphTraversal<?, Boolean> lambdaFunction) {
        this.lambdaFunction = lambdaFunction;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void addNestedFunction(GraphTraversal<?, Boolean> nestedFunction) {
        if (this.nestedFunctions == null) {
            this.nestedFunctions = new ArrayList<>();
        }
        this.nestedFunctions.add(nestedFunction);
    }

    public void setConstraintFunction(GraphTraversal<?, Boolean> constraintFunction) {
        this.constraintFunction = constraintFunction;
    }

    public GraphTraversal<?, Boolean> getConstraintFunction() {
        return constraintFunction;
    }

    public void setRuntimeFunction(String runtimeFunction) {
        this.runtimeFunction = runtimeFunction;
    }
}
