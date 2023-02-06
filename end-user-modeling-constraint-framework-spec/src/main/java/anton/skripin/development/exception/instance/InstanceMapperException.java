package anton.skripin.development.exception.instance;

/**
 * Concrete {@link InstanceException} thrown in case of instance mapping failures.
 */
public class InstanceMapperException extends InstanceException {

    public InstanceMapperException(String message) {
        super(message);
    }

    public InstanceMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstanceMapperException(Throwable cause) {
        super(cause);
    }
}
