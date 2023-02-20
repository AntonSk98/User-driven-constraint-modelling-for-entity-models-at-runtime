package ansk.development.exception.model;

/**
 * Concrete {@link ModelException} thrown in case of failures during model element operations.
 */
public class ModelOperationException extends ModelException {
    public ModelOperationException(String message) {
        super(message);
    }

    public ModelOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelOperationException(Throwable cause) {
        super(cause);
    }
}
