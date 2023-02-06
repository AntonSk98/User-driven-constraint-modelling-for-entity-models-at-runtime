package anton.skripin.development.exception.instance;

/**
 * Concrete {@link InstanceException} thrown in case of instance-related operation failures.
 */
public class InstanceOperationException extends InstanceException {
    public InstanceOperationException(String message) {
        super(message);
    }

    public InstanceOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstanceOperationException(Throwable cause) {
        super(cause);
    }
}
