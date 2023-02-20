package ansk.development.exception.constraint;

/**
 * Abstract constraint exception.
 */
public abstract class ConstraintException extends RuntimeException {
    public ConstraintException(String message) {
        super(message);
    }

    public ConstraintException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstraintException(Throwable cause) {
        super(cause);
    }
}
