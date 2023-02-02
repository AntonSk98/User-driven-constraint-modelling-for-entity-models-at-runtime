package anton.skripin.development.exception;

/**
 * Concrete {@link ModelException} thrown in case of model mapping failures.
 */
public class ModelMapperException extends ModelException {

    public ModelMapperException(String message) {
        super(message);
    }

    public ModelMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelMapperException(Throwable cause) {
        super(cause);
    }
}
