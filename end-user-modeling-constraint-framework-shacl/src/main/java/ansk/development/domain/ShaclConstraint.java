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

import ansk.development.dsl.ShaclConstraintShape;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Container class to transform abstract constraint to its concrete SHACL implementation.
 */
public class ShaclConstraint {

    private ShaclConstraintShape context;
    private Boolean nested;
    private List<String> navigation;
    private Resource lambdaFunction;
    private Map<String, String> params;
    private String attribute;
    private Pair<String, String> runtimeFunction;
    private List<Resource> nestedFunctions;

    public Boolean nested() {
        return nested;
    }

    public Optional<List<String>> navigation() {
        return Optional.ofNullable(navigation);
    }

    public Optional<Resource> lambdaFunction() {
        return Optional.ofNullable(lambdaFunction);
    }

    public Optional<Map<String, String>> params() {
        return Optional.ofNullable(params);
    }

    public Optional<String> attribute() {
        return Optional.ofNullable(attribute);
    }

    public Optional<Pair<String, String>> runtimeFunction() {
        return Optional.ofNullable(runtimeFunction);
    }

    public Optional<List<Resource>> nestedFunctions() {
        return Optional.ofNullable(nestedFunctions);
    }

    public void setNested(Boolean nested) {
        this.nested = nested;
    }

    public void setNavigation(List<String> navigation) {
        this.navigation = navigation;
    }

    public void setLambdaFunction(Resource lambdaFunction) {
        this.lambdaFunction = lambdaFunction;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setRuntimeFunction(String runtimeFunction) {
        this.runtimeFunction = new ImmutablePair<>("###", runtimeFunction);
    }

    public void addNestedFunction(Resource nestedFunction) {
        if (this.nestedFunctions == null) {
            this.nestedFunctions = new ArrayList<>();
        }
        this.nestedFunctions.add(nestedFunction);
    }

    public ShaclConstraintShape getContext() {
        return context;
    }

    public void setContext(ShaclConstraintShape context) {
        this.context = context;
    }
}
