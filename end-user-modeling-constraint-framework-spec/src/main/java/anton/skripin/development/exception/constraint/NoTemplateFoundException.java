package anton.skripin.development.exception.constraint;

import anton.skripin.development.exception.constraint.ConstraintException;

/**
 * Concrete {@link ConstraintException} thrown whenever a required template cannot be found.
 */
public class NoTemplateFoundException extends ConstraintException {

    public NoTemplateFoundException(String message) {
        super(String.format("Function with name %s does not exist in registry. Consider adding it first!"));
    }
}