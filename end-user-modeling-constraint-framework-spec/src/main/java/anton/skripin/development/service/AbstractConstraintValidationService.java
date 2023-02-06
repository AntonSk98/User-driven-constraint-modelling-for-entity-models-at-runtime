package anton.skripin.development.service;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.service.api.ConstraintValidationService;
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
        constraintFunction.booleanFunctions().ifPresent(constraintFunctions -> {
            constraintFunctions
                    .forEach(function -> resolvePathsRecursively(contextType, function, requiredSubgraphElements));
        });
    }

    @Override
    public Object validateConstraint(List<InstanceElement> subgraphForValidation, Constraint constraint) {
        throw new UnsupportedOperationException("It is an abstract validation service!" +
                "Validation is not supported!" +
                "Please use the constraint validation of one of the constraint engine provider or implement it yourself!");
    }
}
