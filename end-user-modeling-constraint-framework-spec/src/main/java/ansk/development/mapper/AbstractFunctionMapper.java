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

package ansk.development.mapper;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Abstract function mapper that links an abstract function name with a concrete platform-specific implementation.
 * @param <Constraint> Constraint that should be evaluated
 * @param <Graph> Graph that is used as a context for constraint evaluation
 */
public abstract class AbstractFunctionMapper<Constraint, Graph> {
    private final Map<String, Function<Constraint, Graph>> functionMapper;

    protected AbstractFunctionMapper() {
        this.functionMapper = new HashMap<>();
        mapFunctions();
    }

    public Function<Constraint, Graph> getFunctionByName(String functionName) {
        return functionMapper.get(functionName);
    }

    private void mapFunctions() {
        functionMapper.putAll(mapCollectionBasedFunctions());
        functionMapper.putAll(mapRangeBasedFunctions());
        functionMapper.putAll(mapLogicalFunctions());
        functionMapper.putAll(mapStringBasedFunctions());
        functionMapper.putAll(mapConditionBasedFunctions());
        functionMapper.putAll(mapAssociationBasedFunctions());
        functionMapper.put(mapRuntimeFunction().getKey(), mapRuntimeFunction().getValue());
    }

    public abstract Map<String, Function<Constraint, Graph>> mapCollectionBasedFunctions();

    public abstract Map<String, Function<Constraint, Graph>> mapRangeBasedFunctions();

    public abstract Map<String, Function<Constraint, Graph>> mapLogicalFunctions();

    public abstract Map<String, Function<Constraint, Graph>> mapStringBasedFunctions();

    public abstract Map<String, Function<Constraint, Graph>> mapConditionBasedFunctions();

    public abstract Map<String, Function<Constraint, Graph>> mapAssociationBasedFunctions();

    public abstract Pair<String, Function<Constraint, Graph>> mapRuntimeFunction();
}
