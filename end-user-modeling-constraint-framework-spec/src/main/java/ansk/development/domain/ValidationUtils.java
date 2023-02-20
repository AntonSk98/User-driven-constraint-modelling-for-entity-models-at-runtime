package ansk.development.domain;

import ansk.development.exception.constraint.function.FunctionValidationException;
import ansk.development.domain.constraint.functions.ConstraintFunction;
import ansk.development.domain.constraint.functions.types.LogicalFunction;
import ansk.development.exception.constraint.function.FunctionException;

/**
 * Provides static methods for validation.
 */
public class ValidationUtils {

    private ValidationUtils() {
    }

    /**
     * Every element that has a navigation parameter must match a certain pattern.
     * Valid navigation examples are: takes_part_in(Sprint), works_on(Project).consists_of(Sprint), and so on.
     *
     * @param navigation path
     */
    public static void validateNavigation(String navigation) {
        assert navigation != null;
        String validNavigationRegex = "(\\w+\\(\\w+\\))(\\.\\w+\\(\\w+\\))*";
        if (navigation.matches(validNavigationRegex)) {
            return;
        }

        String exception = "The provided navigation:" +
                navigation +
                " does not match a required pattern." +
                "\n" +
                "Valid navigation examples are:" +
                "\n\t(1) association(Type)" +
                "\n\t(2) associationOne(Type).associationTwo(Type)";

        throw new FunctionValidationException(exception);
    }

    /**
     * Every element that has an attribute parameter must match a certain pattern.
     * Valid attribute examples are: <Sprint>name, <SoftwareEngineer>age, and so on...
     *
     * @param attribute path
     */
    public static void validateAttribute(String attribute) {
        String validAttributeRegex = "(<\\w+>\\w+)";
        if (attribute.matches(validAttributeRegex)) {
            return;
        }

        String exception = "The provided attribute: " +
                attribute +
                " does not match a required pattern." +
                "\n" +
                "A valid attribute example is:" +
                "<Type>attribute";

        throw new FunctionValidationException(exception);
    }

    /**
     * Enforces that a nested function cannot be of type {@link LogicalFunction} inside a given function.
     *
     * @param function           name
     * @param constraintFunction {@link ConstraintFunction}
     */
    public static void validateIsNotLogicalFunction(String function, ConstraintFunction constraintFunction) {
        if (constraintFunction.booleanFunctions().isPresent()) {
            throw new FunctionException("Logical function is not allowed inside " + function);
        }
    }

    public static void validateLambdaFunctionHasNoNavigation(String function, ConstraintFunction constraintFunction) {
        if (constraintFunction.navigation().isPresent()) {
            throw new FunctionException("Functions with navigations are not supported inside " + function);
        }
        constraintFunction.booleanFunctions()
                .ifPresent(nestedFunctions -> nestedFunctions.forEach(nestedFunction -> validateLambdaFunctionHasNoNavigation(function, nestedFunction)));
    }

    public static void validateLambdaFunctionIsNotLogicalFunction(String function, ConstraintFunction constraintFunction) {
        if (constraintFunction.booleanFunctions().isPresent()) {
            throw new FunctionException("Logical functions are not supported inside " + function);
        }
    }
}
