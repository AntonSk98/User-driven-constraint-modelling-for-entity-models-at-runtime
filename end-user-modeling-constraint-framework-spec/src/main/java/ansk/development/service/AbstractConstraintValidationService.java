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

package ansk.development.service;

import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.ConstraintValidationReport;
import ansk.development.domain.constraint.functions.ConstraintFunction;
import ansk.development.domain.instance.InstanceElement;
import ansk.development.service.api.ConstraintValidationService;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class AbstractConstraintValidationService implements ConstraintValidationService {

    @Override
    public Set<Set<String>> getRequiredSubgraphElements(Constraint constraint) {
        Set<Set<String>> requiredSubgraphElements = new HashSet<>();
        ConstraintFunction constraintFunction = constraint.getConstraintFunction();
        resolvePathsRecursively(constraint.getModelElementType(), constraintFunction, requiredSubgraphElements);
        return requiredSubgraphElements;
    }

    private void resolvePathsRecursively(String contextType, ConstraintFunction constraintFunction, Set<Set<String>> requiredSubgraphElements) {
        LinkedHashSet<String> typesForSubgraph = new LinkedHashSet<>();
        typesForSubgraph.add(contextType);

        addToPath(constraintFunction, typesForSubgraph);

        constraintFunction.lambdaFunction().ifPresent(lambdaFunction -> {
            addToPath(lambdaFunction, typesForSubgraph);
        });

        if (typesForSubgraph.size() > 0) {
            requiredSubgraphElements.add(typesForSubgraph);
        }
        resolveNestedPaths(contextType, constraintFunction, requiredSubgraphElements);
    }

    private void addToPath(ConstraintFunction constraintFunction, LinkedHashSet<String> typesForSubgraph) {
        constraintFunction
                .params()
                .stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .filter(parameter -> parameter.contains("(") && parameter.contains(")"))
                .map(parameter -> StringUtils.substringsBetween(parameter, "(", ")"))
                .flatMap(Arrays::stream)
                .forEach(typesForSubgraph::add);

        constraintFunction.navigation().ifPresent(path -> {
            var navigationTypes = Optional.ofNullable(StringUtils.substringsBetween(path, "(", ")"));
            navigationTypes.ifPresent(types -> typesForSubgraph.addAll(List.of(types)));
        });

        constraintFunction.attribute().ifPresent(attribute -> {
            var attributeType = Optional.ofNullable(StringUtils.substringBetween(attribute, "<", ">"));
            attributeType.ifPresent(typesForSubgraph::add);
        });
    }

    private void resolveNestedPaths(String contextType, ConstraintFunction constraintFunction, Set<Set<String>> requiredSubgraphElements) {
        constraintFunction.booleanFunctions().ifPresent(constraintFunctions -> constraintFunctions
                .forEach(function -> resolvePathsRecursively(contextType, function, requiredSubgraphElements)));
    }

    @Override
    public ConstraintValidationReport validateConstraint(String uuid, List<InstanceElement> subgraphForValidation, Constraint constraint) {
        throw new UnsupportedOperationException("It is an abstract validation service!" +
                "Validation is not supported!" +
                "Please use the constraint validation of one of the constraint engine provider or implement it yourself!");
    }
}
