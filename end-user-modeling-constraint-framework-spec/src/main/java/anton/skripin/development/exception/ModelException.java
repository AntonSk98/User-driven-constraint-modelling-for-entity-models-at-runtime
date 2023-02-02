package anton.skripin.development.exception;

/**
 * Abstract exception for model element failures.
 */
public abstract class ModelException extends RuntimeException {
    public ModelException(String message) {
        super(message);
    }

    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelException(Throwable cause) {
        super(cause);
    }
}
