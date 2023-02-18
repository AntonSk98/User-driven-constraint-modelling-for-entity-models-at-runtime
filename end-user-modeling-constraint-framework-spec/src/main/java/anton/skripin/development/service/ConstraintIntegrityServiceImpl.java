package anton.skripin.development.service;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import anton.skripin.development.domain.integrity.IntegrityReport;
import anton.skripin.development.service.api.ConstraintIntegrityService;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConstraintIntegrityServiceImpl implements ConstraintIntegrityService {

    private static boolean isOldPropertyPresentAndTypeMatches(String modelElementName, String constraintPart, String oldPropertyName) {
        return constraintPart.contains(oldPropertyName) && constraintPart.contains(modelElementName);
    }

    private static boolean isOldPropertyPresent(String constraintPart, String oldPropertyName) {
        return constraintPart.contains(oldPropertyName);
    }

    @SneakyThrows
    private static void updateField(ConstraintFunction constraintFunction, String fieldName, String value) {
        Field field = constraintFunction.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(constraintFunction, value);
    }

    private static String replaceProperty(String constraintPart, String oldProperty, String newProperty) {
        return constraintPart.replace(oldProperty, newProperty);
    }

    @Override
    public IntegrityReport synchronizeConstraints(String modelElement, String oldPropertyName, String newPropertyName, List<Constraint> constraints) {
        Set<Constraint> updatedConstraints = new HashSet<>();
        constraints.forEach(constraint -> {
            if (synchronizeFunction(modelElement, constraint.getConstraintFunction(), oldPropertyName, newPropertyName)) {
                updatedConstraints.add(constraint);
            }
        });
        return new IntegrityReport(modelElement, oldPropertyName, newPropertyName, updatedConstraints);
    }

    private boolean synchronizeFunction(String modelElement, ConstraintFunction constraintFunction, String oldPropertyName, String newPropertyName) {
        AtomicBoolean updated = new AtomicBoolean(false);
        constraintFunction
                .attribute()
                .filter(attribute -> isOldPropertyPresentAndTypeMatches(modelElement, attribute, oldPropertyName))
                .ifPresent(attribute -> {
                    updateField(constraintFunction, "attribute", replaceProperty(attribute, oldPropertyName, newPropertyName));
                    updated.set(true);
                });
        constraintFunction
                .navigation()
                .filter(navigation -> isOldPropertyPresent(navigation, oldPropertyName))
                .ifPresent(navigation -> {
                    updateField(constraintFunction, "navigation", replaceProperty(navigation, oldPropertyName, newPropertyName));
                    updated.set(true);
                });
        constraintFunction
                .lambdaFunction()
                .ifPresent(lambdaFunction -> {
                    if (synchronizeFunction(modelElement, lambdaFunction, oldPropertyName, newPropertyName)) {
                        updated.set(true);
                    }
                });
        constraintFunction
                .booleanFunctions()
                .ifPresent(booleanFunctions -> booleanFunctions
                        .forEach(booleanFunction -> {
                            if (synchronizeFunction(modelElement, booleanFunction, oldPropertyName, newPropertyName)) {
                                updated.set(true);
                            }
                        }));
        return updated.get();
    }

    @Override
    public IntegrityReport getInvalidConstraints(String modelElement, String removedPropertyName, List<Constraint> constraints) {
        Set<Constraint> invalidConstraints = new HashSet<>();
        constraints.forEach(constraint -> {
            if (getInvalidFunctions(modelElement, constraint.getConstraintFunction(), removedPropertyName)) {
                invalidConstraints.add(constraint);
            }
        });
        return new IntegrityReport(modelElement, removedPropertyName, invalidConstraints);
    }

    private boolean getInvalidFunctions(String modelElement, ConstraintFunction constraintFunction, String removedPropertyName) {
        AtomicBoolean invalid = new AtomicBoolean(true);
        constraintFunction
                .attribute()
                .filter(attribute -> isOldPropertyPresentAndTypeMatches(modelElement, attribute, removedPropertyName))
                .ifPresent(attribute -> invalid.set(true));

        constraintFunction
                .navigation()
                .filter(navigation -> isOldPropertyPresent(navigation, removedPropertyName))
                .ifPresent(navigation -> invalid.set(true));

        constraintFunction
                .lambdaFunction()
                .ifPresent(lambdaFunction -> {
                    if (getInvalidFunctions(modelElement, lambdaFunction, removedPropertyName)) {
                        invalid.set(true);
                    }
                });
        constraintFunction
                .booleanFunctions()
                .ifPresent(booleanFunctions -> booleanFunctions.forEach(booleanFunction -> {
                    if (getInvalidFunctions(modelElement, booleanFunction, removedPropertyName)) {
                        invalid.set(true);
                    }
                }));
        return invalid.get();
    }
}
