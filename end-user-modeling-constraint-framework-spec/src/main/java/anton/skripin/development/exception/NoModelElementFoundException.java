package anton.skripin.development.exception;

/**
 * Concrete {@link ModelException} thrown whenever a model element does not exist.
 */
public class NoModelElementFoundException extends ModelException {
    public NoModelElementFoundException(String message) {
        super(message);
    }
}
