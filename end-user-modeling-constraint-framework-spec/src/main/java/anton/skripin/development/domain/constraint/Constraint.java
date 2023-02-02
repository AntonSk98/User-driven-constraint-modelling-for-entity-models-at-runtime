package anton.skripin.development.domain.constraint;

import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Defines a constraint definition.
 */
@Getter
@Setter
@NoArgsConstructor
public class Constraint {
    /**
     * Unique identifier
     */
    private String uuid;
    /**
     * Name of a constraint
     */
    private String name;
    /**
     * UUID of a model element a constraint is applied to
     */
    private String modelElementUuid;
    /**
     * Type a model element a constraint is applied to
     */
    private String modelElementType;
    /**
     * Message that is reported in case of a constraint violation
     */
    private String violationMessage;
    /**
     * Sets {@link ViolationLevel}.
     */
    private ViolationLevel violationLevel;
    /**
     * Every constraint has either one function or nested function with several constraints. See {@link ConstraintFunction}
     */
    private ConstraintFunction constraintFunction;
}
