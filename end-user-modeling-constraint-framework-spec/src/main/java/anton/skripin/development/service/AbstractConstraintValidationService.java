package anton.skripin.development.service;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import anton.skripin.development.domain.constraint.functions.ContextConstraintFunction;
import anton.skripin.development.domain.constraint.functions.HierarchicalConstraintFunction;
import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.service.api.ConstraintValidationService;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static anton.skripin.development.domain.constraint.functions.ConstraintFunction.toContextConstraintFunction;
import static anton.skripin.development.domain.constraint.functions.ConstraintFunction.toHierarchicalFunction;

public class AbstractConstraintValidationService implements ConstraintValidationService {

    @Override
    public Set<List<String>> getRequiredSubgraphElements(Constraint constraint) {
        Set<List<String>> requiredSubgraphElements = new HashSet<>();
        ConstraintFunction constraintFunction = constraint.getConstraintFunction();
        resolvePathsRecursively(requiredSubgraphElements, constraintFunction);
        return requiredSubgraphElements;
    }

    private void resolvePathsRecursively(Set<List<String>> requiredSubgraphElements, ConstraintFunction constraintFunction) {
        if (constraintFunction.isContextConstraintFunction()) {
            ContextConstraintFunction contextConstraintFunction = toContextConstraintFunction(constraintFunction);
            String path = contextConstraintFunction.getPath();
            List<String> requiredTypes = List.of(StringUtils.substringsBetween(path, "(", ")"));
            requiredSubgraphElements.add(requiredTypes);
        }
        if (constraintFunction.isHierarchicalFunction()) {
            HierarchicalConstraintFunction hierarchicalConstraintFunction = toHierarchicalFunction(constraintFunction);
            hierarchicalConstraintFunction
                    .getNestedFunctions()
                    .forEach(nestedFunction -> resolvePathsRecursively(requiredSubgraphElements, nestedFunction));
        }
    }

    @Override
    public Object validateConstraint(List<InstanceElement> subgraphForValidation, Constraint constraint) {
        throw new UnsupportedOperationException("It is an abstract validation service!" +
                "Validation is not supported!" +
                "Please use the constraint validation of one of the constraint engine provider or implement it yourself!");
    }
}
