package ansk.development.exception.constraint;

/**
 * Concrete {@link ConstraintException} thrown in case of parsing failures.
 */
public class ConstraintParsingException extends ConstraintException {

    public ConstraintParsingException(String uuid) {
        super(String.format("Failed to deserialize user-defined constraint from template with id %s", uuid));
    }
}
