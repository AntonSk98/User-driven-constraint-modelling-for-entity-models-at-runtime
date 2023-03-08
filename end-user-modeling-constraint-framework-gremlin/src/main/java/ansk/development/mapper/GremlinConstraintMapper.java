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

import ansk.development.GremlinRegistry;
import ansk.development.domain.AttributeUtils;
import ansk.development.domain.GremlinConstraint;
import ansk.development.domain.NavigationUtils;
import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.functions.ConstraintFunction;
import ansk.development.domain.instance.InstanceElement;
import ansk.development.dsl.ConstraintGraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

import static ansk.development.domain.constraint.functions.FunctionType.RUNTIME_FUNCTION;

/**
 * Implementation of {@link AbstractToPSConstraintMapper}.
 */
public class GremlinConstraintMapper implements AbstractToPSConstraintMapper<ConstraintGraphTraversalSource, GraphTraversal<?, Boolean>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GremlinConstraintMapper.class);

    private static final GremlinFunctionMapper GREMLIN_FUNCTION_MAPPER = new GremlinFunctionMapper();

    private static void measureExecutionTime(String name, Runnable function) {
        long startTime = System.currentTimeMillis();
        function.run();
        long finishTime = System.currentTimeMillis();
        LOGGER.info("Execution time for '{}' is {} ms", name, finishTime - startTime);
    }

    @Override
    public GraphTraversal<?, Boolean> mapToPlatformSpecificConstraint(String uuid, Constraint constraint) {
        ConstraintFunction constraintFunction = constraint.getConstraintFunction();
        return mapFunction(uuid, constraintFunction, true);
    }

    private GraphTraversal<?, Boolean> mapFunction(String uuid, ConstraintFunction constraintFunction, boolean traversalStart) {
        GremlinConstraint gremlinConstraint = new GremlinConstraint();
        if (traversalStart || constraintFunction.booleanFunctions().isPresent() || constraintFunction.runtimeFunction().isPresent()) {
            gremlinConstraint.setContext(GremlinRegistry.getConstraintTraversal().instance(uuid));
        }
        constraintFunction.runtimeFunction().ifPresent(gremlinConstraint::setRuntimeFunction);
        constraintFunction.attribute().map(AttributeUtils::getAttributeRoot).ifPresent(gremlinConstraint::setAttribute);
        constraintFunction.navigation().map(NavigationUtils::getNavigationRoot).ifPresent(gremlinConstraint::setNavigation);
        constraintFunction.lambdaFunction().ifPresent(lambdaFunction -> gremlinConstraint.setLambdaFunction(mapFunction(uuid, lambdaFunction, false)));
        constraintFunction.booleanFunctions().ifPresent(booleanFunctions -> {
            booleanFunctions.forEach(booleanFunction -> gremlinConstraint.addNestedFunction(mapFunction(uuid, booleanFunction, false)));
        });
        constraintFunction.params().ifPresent(gremlinConstraint::setParams);
        if (constraintFunction.runtimeFunction().isPresent()) {
            gremlinConstraint.setTraversal(GREMLIN_FUNCTION_MAPPER.getFunctionByName(RUNTIME_FUNCTION).apply(gremlinConstraint));
        } else {
            gremlinConstraint.setTraversal(GREMLIN_FUNCTION_MAPPER.getFunctionByName(constraintFunction.getName()).apply(gremlinConstraint));
        }
        return gremlinConstraint.getTraversal();
    }

    @Override
    public ConstraintGraphTraversalSource mapToPlatformSpecificGraph(List<InstanceElement> instanceElementGraph) {
        GremlinRegistry.spawnNewGraph();
        LOGGER.info("Total number of graph elements to be mapped: {}", instanceElementGraph.size());
        ConstraintGraphTraversalSource graphSource = GremlinRegistry.getConstraintTraversal();
        measureExecutionTime("create elements", () -> instanceElementGraph.forEach(graphSource::addInstance));

        measureExecutionTime("create links", () -> instanceElementGraph
                .stream()
                .filter(instanceElement -> Objects.nonNull(instanceElement.getLinks()))
                .flatMap(instanceElement -> instanceElement.getLinks().stream())
                .forEach(graphSource::linkTwoInstances));


        return graphSource;
    }


}
