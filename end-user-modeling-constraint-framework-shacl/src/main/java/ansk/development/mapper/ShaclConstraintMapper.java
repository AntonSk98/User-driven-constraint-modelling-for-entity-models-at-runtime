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
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

import static ansk.development.domain.constraint.functions.FunctionType.RUNTIME_FUNCTION;

/**
 * Implementation of {@link AbstractToPSConstraintMapper}.
 */
public class ShaclConstraintMapper implements AbstractToPSConstraintMapper<ShaclConstraintData, ShaclConstraintShape> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShaclConstraintMapper.class);

    private static void measureExecutionTime(String name, Runnable function) {
        long startTime = System.currentTimeMillis();
        function.run();
        long finishTime = System.currentTimeMillis();
        LOGGER.info("Execution time for '{}' is {} ms", name, finishTime - startTime);
    }
    @Override
    public ShaclConstraintShape mapToPlatformSpecificConstraint(String instanceUuid, Constraint constraint) {
        ConstraintFunction constraintFunction = constraint.getConstraintFunction();
        Resource shaclConstraint = mapFunction(instanceUuid, constraintFunction, true, new ShaclConstraintShape());
        return (ShaclConstraintShape) shaclConstraint.getModel();
    }

    private Resource mapFunction(String instanceUuid, ConstraintFunction constraintFunction, boolean initial, ShaclConstraintShape shaclConstraintShape) {
        ShaclConstraint shaclConstraint = new ShaclConstraint();
        shaclConstraint.setNested(!initial);
        shaclConstraint.setContext(shaclConstraintShape);
        if (initial) {
            shaclConstraintShape.getTargetInstance(instanceUuid);
        }
        Resource resource;
        constraintFunction.runtimeFunction().ifPresent(shaclConstraint::setRuntimeFunction);
        constraintFunction.attribute().map(AttributeUtils::getAttributeRoot).ifPresent(shaclConstraint::setAttribute);
        constraintFunction.navigation().map(NavigationUtils::getNavigationRoot).ifPresent(shaclConstraint::setNavigation);
        constraintFunction
                .lambdaFunction()
                .ifPresent(lambdaFunction -> shaclConstraint.setLambdaFunction(mapFunction(instanceUuid, lambdaFunction, false, shaclConstraintShape)));
        constraintFunction.booleanFunctions().ifPresent(booleanFunctions -> {
            booleanFunctions.forEach(booleanFunction -> shaclConstraint.addNestedFunction(mapFunction(instanceUuid, booleanFunction, false, shaclConstraintShape)));
        });
        constraintFunction.params().ifPresent(shaclConstraint::setParams);
        if (constraintFunction.runtimeFunction().isPresent()) {
            resource = ShaclFunctionMapper.CONSTRAINTS_MAP.get(RUNTIME_FUNCTION).apply(shaclConstraint);
        } else {
            resource = ShaclFunctionMapper.CONSTRAINTS_MAP.get(constraintFunction.getName()).apply(shaclConstraint);
        }
        return resource;
    }

    @Override
    public ShaclConstraintData mapToPlatformSpecificGraph(List<InstanceElement> graph) {
        LOGGER.info("Total number of graph elements to be mapped: {}", graph.size());
        ShaclConstraintData model = new ShaclConstraintData();
        measureExecutionTime("create elements", () -> graph.forEach(model::createInstance));
        measureExecutionTime("create links", () -> graph
                .stream()
                .filter(instanceElement -> Objects.nonNull(instanceElement.getLinks()))
                .flatMap(instanceElement -> instanceElement.getLinks().stream())
                .forEach(model::addLinkToInstance));
        return model;
    }
}
