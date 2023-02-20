package ansk.development.exception.constraint;

/**
 * Concrete {@link ConstraintException} thrown whenever a constraint is failed to be persisted.
 */
public class PersistConstraintException extends ConstraintException {

    public PersistConstraintException(String message) {
        super(message);
    }

    public PersistConstraintException(String message, Throwable cause) {
        super(message, cause);
    }
}
