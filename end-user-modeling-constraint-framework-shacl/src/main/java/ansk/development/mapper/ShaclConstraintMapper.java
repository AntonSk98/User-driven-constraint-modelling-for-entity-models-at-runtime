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

import ansk.development.domain.*;
import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.functions.ConstraintFunction;
import ansk.development.domain.instance.InstanceElement;
import ansk.development.dsl.ShaclConstraintShape;
import ansk.development.dsl.ShaclInstanceGraph;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static ansk.development.domain.constraint.functions.FunctionType.RUNTIME_FUNCTION;

/**
 * Implementation of {@link AbstractToPSConstraintMapper}.
 */
public class ShaclConstraintMapper implements AbstractToPSConstraintMapper<ShaclInstanceGraph, ShaclConstraintShape> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShaclConstraintMapper.class);
    private static final ShaclFunctionMapper SHACL_FUNCTION_MAPPER = new ShaclFunctionMapper();

    private static void measureExecutionTime(String name, Runnable function) {
        long startTime = System.currentTimeMillis();
        function.run();
        long finishTime = System.currentTimeMillis();
        LOGGER.info("Execution time for '{}' is {} ms", name, finishTime - startTime);
    }

    @Override
    public ShaclConstraintShape mapToPlatformSpecificConstraint(String instanceUuid, Constraint constraint) {
        ConstraintFunction constraintFunction = constraint.getConstraintFunction();
        Resource shaclConstraint = mapFunction(instanceUuid, constraintFunction, new ShaclConstraintShape(), true);
        return (ShaclConstraintShape) shaclConstraint.getModel();
    }

    private Resource mapFunction(String uuid, ConstraintFunction function, ShaclConstraintShape shape, boolean... initial) {
        boolean initialFlag = initial.length == 1 && initial[0];
        ShaclConstraint shaclConstraint = new ShaclConstraint();
        shaclConstraint.setNested(!initialFlag);
        shaclConstraint.setContext(shape);
        shape.loadTargetInstanceByCondition(uuid, () -> initialFlag);
        function.runtimeFunction().ifPresent(shaclConstraint::setRuntimeFunction);
        function.attribute().map(AttributeUtils::getAttributeRoot).ifPresent(shaclConstraint::setAttribute);
        function.navigation().map(NavigationUtils::getNavigationRoot).ifPresent(shaclConstraint::setNavigation);
        function
                .lambdaFunction()
                .map(lambdaFunction -> mapFunction(uuid, lambdaFunction, shape))
                .ifPresent(shaclConstraint::setLambdaFunction);
        function.booleanFunctions().ifPresent(booleanFunctions -> booleanFunctions
                .forEach(booleanFunction -> shaclConstraint
                        .addNestedFunction(mapFunction(uuid, booleanFunction, shape))));
        function.params().ifPresent(shaclConstraint::setParams);
        Function<ShaclConstraint, Resource> mappedFunction = function.runtimeFunction().isPresent()
                ? SHACL_FUNCTION_MAPPER.getFunctionByName(RUNTIME_FUNCTION)
                : SHACL_FUNCTION_MAPPER.getFunctionByName(function.getName());
        return mappedFunction.apply(shaclConstraint);
    }

    @Override
    public ShaclInstanceGraph mapToPlatformSpecificGraph(List<InstanceElement> graph) {
        LOGGER.info("Total number of graph elements to be mapped: {}", graph.size());
        ShaclInstanceGraph model = new ShaclInstanceGraph();
        measureExecutionTime("create elements", () -> graph.forEach(model::createInstance));
        measureExecutionTime("create links", () -> graph
                .stream()
                .filter(instanceElement -> Objects.nonNull(instanceElement.getLinks()))
                .flatMap(instanceElement -> instanceElement.getLinks().stream())
                .forEach(model::addLinkToInstance));
        return model;
    }
}
