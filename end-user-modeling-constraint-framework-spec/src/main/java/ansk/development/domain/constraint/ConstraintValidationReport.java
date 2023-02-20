package ansk.development.domain.constraint;

import lombok.Getter;

/**
 * POJO that is returned as a result of a constraint violation.
 */
@Getter
public class ConstraintValidationReport {
    private final String name;
    private final String elementType;
    private final ValidationResult result;
    private final boolean isValid;
    private final String violationMessage;

    /**
     * Constructor
     * @param name of a constraint
     * @param elementType context element which a constraint is applied to
     * @param isValid whether evaluation result is true
     * @param violationLevel See {@link ViolationLevel}
     * @param violationMessage Message that should be returned to an end-user in case a constraint is invalid
     */
    public ConstraintValidationReport(String name, String elementType, boolean isValid, ViolationLevel violationLevel, String violationMessage) {
        this.name = name;
        this.elementType = elementType;
        this.isValid = isValid;
        this.result = resolveResult(isValid, violationLevel);
        this.violationMessage = violationMessage;
    }

    private static ValidationResult resolveResult(boolean isValid, ViolationLevel result) {
        if (isValid) {
            return ValidationResult.VALID;
        }
        if (result.equals(ViolationLevel.WARN)) {
            return ValidationResult.WARN;
        }
        if (result.equals(ViolationLevel.ERROR)) {
            return ValidationResult.INVALID;
        }
        throw new RuntimeException("Unknown violation level: " + result);
    }
}
