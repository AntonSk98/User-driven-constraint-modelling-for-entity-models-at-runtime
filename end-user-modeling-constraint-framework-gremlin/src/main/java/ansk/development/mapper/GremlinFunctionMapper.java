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

import ansk.development.domain.GremlinConstraint;
import ansk.development.domain.NavigationUtils;
import ansk.development.dsl.ConstraintGraphTraversal;
import ansk.development.dsl.__;
import ansk.development.exception.constraint.GraphConstraintException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tinkerpop.gremlin.groovy.engine.GremlinExecutor;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;

import javax.script.SimpleBindings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static ansk.development.domain.constraint.functions.FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES;
import static ansk.development.domain.constraint.functions.FunctionMetadata.FunctionNames.*;
import static ansk.development.domain.constraint.functions.FunctionType.RUNTIME_FUNCTION;

/**
 * Maps function with its concrete Gremlin implementation.
 */
public class GremlinFunctionMapper extends AbstractFunctionMapper<GremlinConstraint, GraphTraversal<?, Boolean>> {

    @Override
    public Map<String, Function<GremlinConstraint, GraphTraversal<?, Boolean>>> mapRangeBasedFunctions() {
        Map<String, Function<GremlinConstraint, GraphTraversal<?, Boolean>>> mapper = new HashMap<>();

        mapper.put(GREATER_THAN, gremlinConstraint -> {
            String attribute = gremlinConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("GreaterThan() must have an attribute!"));
            String value = gremlinConstraint.params()
                    .orElseThrow(() -> new GraphConstraintException("GreaterThan() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(GREATER_THAN).get(0));
            return gremlinConstraint.context().isPresent() ? gremlinConstraint.context().get().greaterThan(attribute, value) : __.greaterThan(attribute, value);
        });
        mapper.put(GREATER_THAN_OR_EQUALS, gremlinConstraint -> {
            String attribute = gremlinConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("GreaterThanOrEquals() must have an attribute!"));
            String value = gremlinConstraint.params()
                    .orElseThrow(() -> new GraphConstraintException("GreaterThanOrEquals() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(GREATER_THAN_OR_EQUALS).get(0));
            return gremlinConstraint.context().isPresent() ? gremlinConstraint.context().get().greaterThanOrEquals(attribute, value) : __.greaterThanOrEquals(attribute, value);
        });
        mapper.put(LESS_THAN, gremlinConstraint -> {
            String attribute = gremlinConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("LessThan() must have an attribute!"));
            String value = gremlinConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("LessThan() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(LESS_THAN).stream().findFirst().get());
            return gremlinConstraint.context().isPresent() ?
                    gremlinConstraint.context().get().lessThan(attribute, value) :
                    __.lessThan(attribute, value);
        });
        mapper.put(LESS_THAN_OR_EQUALS, gremlinConstraint -> {
            String attribute = gremlinConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("LessThanOrEquals() must have an attribute!"));
            String value = gremlinConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("LessThanOrEquals() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(LESS_THAN_OR_EQUALS).get(0));
            return gremlinConstraint.context().isPresent() ? gremlinConstraint.context().get().lessThanOrEquals(attribute, value) : __.lessThanOrEquals(attribute, value);
        });
        mapper.put(EQUALS, gremlinConstraint -> {
            String attribute = gremlinConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("Equals() must have an attribute!"));
            String value = gremlinConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("Equals() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(EQUALS).get(0));
            return gremlinConstraint.context().isPresent() ? gremlinConstraint.context().get().equals(attribute, value) : __.equals(attribute, value);
        });
        return mapper;
    }

    @Override
    public Map<String, Function<GremlinConstraint, GraphTraversal<?, Boolean>>> mapCollectionBasedFunctions() {
        Map<String, Function<GremlinConstraint, GraphTraversal<?, Boolean>>> mapper = new HashMap<>();

        mapper.put(FOR_ALL, gremlinConstraint -> {
            List<String> navigation = gremlinConstraint
                    .navigation()
                    .orElseThrow(() -> new GraphConstraintException("ForAll() must have a navigation"));
            var lambdaFunction = gremlinConstraint
                    .lambdaFunction()
                    .orElseThrow(() -> new GraphConstraintException("ForAll() must have a lambda function"));
            return gremlinConstraint.context().isPresent() ? gremlinConstraint.context().get().forAll(navigation, lambdaFunction) : __.forAll(navigation, lambdaFunction);

        });

        mapper.put(FOR_SOME, gremlinConstraint -> {
            List<String> navigation = gremlinConstraint
                    .navigation()
                    .orElseThrow(() -> new GraphConstraintException("ForSome() must have a navigation"));
            var lambdaFunction = gremlinConstraint
                    .lambdaFunction()
                    .orElseThrow(() -> new GraphConstraintException("ForSome() must have a lambda function"));
            return gremlinConstraint.context().isPresent() ? gremlinConstraint.context().get().forSome(navigation, lambdaFunction) : __.forSome(navigation, lambdaFunction);
        });

        mapper.put(FOR_NONE, gremlinConstraint -> {
            List<String> navigation = gremlinConstraint
                    .navigation()
                    .orElseThrow(() -> new GraphConstraintException("ForNone() must have a navigation"));
            var lambdaFunction = gremlinConstraint
                    .lambdaFunction()
                    .orElseThrow(() -> new GraphConstraintException("ForNone() must have a lambda function"));
            return gremlinConstraint.context().isPresent() ? gremlinConstraint.context().get().forNone(navigation, lambdaFunction) : __.forNone(navigation, lambdaFunction);
        });

        mapper.put(FOR_EXACTLY, gremlinConstraint -> {
            List<String> navigation = gremlinConstraint
                    .navigation()
                    .orElseThrow(() -> new GraphConstraintException("ForExactly() must have a navigation"));
            var lambdaFunction = gremlinConstraint
                    .lambdaFunction()
                    .orElseThrow(() -> new GraphConstraintException("ForExactly() must have a lambda function"));
            Integer matchNumber = Integer
                    .parseInt(gremlinConstraint.params()
                            .orElseThrow(() -> new GraphConstraintException("ForExactly() must have a 'match_number' parameter"))
                            .get(FUNCTION_TO_PARAMETER_NAMES.get(FOR_EXACTLY).get(0)));
            return gremlinConstraint.context().isPresent() ? gremlinConstraint.context().get().forExactly(navigation, lambdaFunction, matchNumber) : __.forExactly(navigation, lambdaFunction, matchNumber);
        });

        return mapper;
    }

    @Override
    public Map<String, Function<GremlinConstraint, GraphTraversal<?, Boolean>>> mapLogicalFunctions() {
        Map<String, Function<GremlinConstraint, GraphTraversal<?, Boolean>>> mapper = new HashMap<>();
        mapper.put(AND, gremlinConstraint -> {
            var context = gremlinConstraint
                    .context()
                    .orElseThrow(() -> new GraphConstraintException("And() cannot be used without a context element"));
            var nestedFunction = gremlinConstraint
                    .nestedFunctions()
                    .orElseThrow(() -> new GraphConstraintException("And() cannot be used without a context element"));
            return context.and(nestedFunction);
        });

        mapper.put(OR, gremlinConstraint -> {
            var context = gremlinConstraint
                    .context()
                    .orElseThrow(() -> new GraphConstraintException("Or() cannot be used without a context element"));
            var nestedFunction = gremlinConstraint
                    .nestedFunctions()
                    .orElseThrow(() -> new GraphConstraintException("Or() cannot be used without a context element"));
            return context.or(nestedFunction);
        });
        return mapper;
    }

    @Override
    public Map<String, Function<GremlinConstraint, GraphTraversal<?, Boolean>>> mapStringBasedFunctions() {
        Map<String, Function<GremlinConstraint, GraphTraversal<?, Boolean>>> mapper = new HashMap<>();

        mapper.put(UNIQUE, gremlinConstraint -> {
            ConstraintGraphTraversal<?, ?> context = gremlinConstraint
                    .context()
                    .orElseThrow(() -> new GraphConstraintException("This function cannot be used without target context!"));

            String attribute = gremlinConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("Unique() must have an attribute!"));
            return context.unique(attribute);
        });

        mapper.put(NOT_NULL_OR_EMPTY, gremlinConstraint -> {
            String attribute = gremlinConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("NotNullOrEmpty() must have an attribute!"));
            return gremlinConstraint.context().isPresent() ? gremlinConstraint.context().get().notNullOrEmpty(attribute) : __.notNullOrEmpty(attribute);
        });

        mapper.put(MAX_LENGTH, gremlinConstraint -> {
            String attribute = gremlinConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("MaxLength() must have an attribute!"));
            String length = gremlinConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("MaxLength() must have a parameter"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(MAX_LENGTH).get(0));
            return gremlinConstraint.context().isPresent() ? gremlinConstraint.context().get().maxLength(attribute, length) : __.maxLength(attribute, length);
        });

        mapper.put(MIN_LENGTH, gremlinConstraint -> {
            String attribute = gremlinConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("MinLength() must have an attribute!"));
            String length = gremlinConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("MinLength() must have a parameter"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(MIN_LENGTH).get(0));
            return gremlinConstraint.context().isPresent() ? gremlinConstraint.context().get().minLength(attribute, length) : __.minLength(attribute, length);
        });

        return mapper;
    }

    @Override
    public Map<String, Function<GremlinConstraint, GraphTraversal<?, Boolean>>> mapConditionBasedFunctions() {
        Map<String, Function<GremlinConstraint, GraphTraversal<?, Boolean>>> mapper = new HashMap<>();

        mapper.put(IF_THEN, gremlinConstraint -> {
            var context = gremlinConstraint
                    .context()
                    .orElseThrow(() -> new GraphConstraintException("IfThen() cannot be used without a context element"));
            var clauses = gremlinConstraint
                    .nestedFunctions()
                    .filter(graphTraversals -> graphTraversals.size() == 2)
                    .orElseThrow(() -> new GraphConstraintException("IfThen() cannot be used without clauses"));
            return context.ifThen(clauses.get(0), clauses.get(1));
        });

        mapper.put(IF_THEN_ELSE, gremlinConstraint -> {
            var context = gremlinConstraint
                    .context()
                    .orElseThrow(() -> new GraphConstraintException("IfThenElse() cannot be used without a context element"));
            var clauses = gremlinConstraint
                    .nestedFunctions()
                    .filter(graphTraversals -> graphTraversals.size() == 3)
                    .orElseThrow(() -> new GraphConstraintException("IfThenElse() cannot be used without clauses"));
            return context.ifThenElse(clauses.get(0), clauses.get(1), clauses.get(2));
        });
        return mapper;
    }

    @Override
    public Map<String, Function<GremlinConstraint, GraphTraversal<?, Boolean>>> mapAssociationBasedFunctions() {
        Map<String, Function<GremlinConstraint, GraphTraversal<?, Boolean>>> mapper = new HashMap<>();

        mapper.put(MIN_CARDINALITY, gremlinConstraint -> {
            var params = gremlinConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("MinCardinality() must have two value attribute"));
            String association = NavigationUtils
                    .getNavigationRoot(params.get(FUNCTION_TO_PARAMETER_NAMES.get(MIN_CARDINALITY).get(0)))
                    .get(0);
            var context = gremlinConstraint
                    .context()
                    .orElseThrow(() -> new GraphConstraintException("This function cannot be used without target context!"));
            Integer minValue = Integer.valueOf(params.get(FUNCTION_TO_PARAMETER_NAMES.get(MIN_CARDINALITY).get(1)));
            return context.minCardinality(association, minValue);
        });

        mapper.put(MAX_CARDINALITY, gremlinConstraint -> {
            var params = gremlinConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("MaxCardinality() must have two value attribute"));
            String association = NavigationUtils
                    .getNavigationRoot(params.get(FUNCTION_TO_PARAMETER_NAMES.get(MAX_CARDINALITY).get(0)))
                    .get(0);
            var context = gremlinConstraint
                    .context()
                    .orElseThrow(() -> new GraphConstraintException("This function cannot be used without target context!"));
            Integer maxValue = Integer.valueOf(params.get(FUNCTION_TO_PARAMETER_NAMES.get(MAX_CARDINALITY).get(1)));
            return context.maxCardinality(association, maxValue);
        });

        return mapper;
    }

    @Override
    public Pair<String, Function<GremlinConstraint, GraphTraversal<?, Boolean>>> mapRuntimeFunction() {
        return new ImmutablePair<>(RUNTIME_FUNCTION, gremlinConstraint -> {
            var context = gremlinConstraint
                    .context()
                    .orElseThrow(() -> new GraphConstraintException("Runtime function cannot be used without a context element"));
            var runtimeFunction = gremlinConstraint
                    .runtimeFunction()
                    .orElseThrow(() -> new GraphConstraintException("No function is provided for runtime function"));
            GremlinExecutor runtimeExecutor = GremlinExecutor
                    .build()
                    .evaluationTimeout(15000L)
                    .globalBindings(new SimpleBindings(Map.of("target", context)))
                    .create();
            String runtimeConstraint = String.format("target.%s.count()", runtimeFunction);
            try {
                GraphTraversal<?, Long> traversal = (GraphTraversal<?, Long>) runtimeExecutor.eval(runtimeConstraint).get();
                Long result = traversal.asAdmin().clone().next();
                return result > 0 ? traversal.constant(true) : traversal.constant(false);
            } catch (InterruptedException | ExecutionException e) {
                throw new GraphConstraintException("Error occurred while evaluating runtime constraint", e);
            }
        });
    }
}
