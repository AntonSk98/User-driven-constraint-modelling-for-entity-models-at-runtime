package ansk.development.exception.instance;

/**
 * General {@link InstanceException} for instance-related errors.
 */
public abstract class InstanceException extends RuntimeException {
    public InstanceException(String message) {
        super(message);
    }

    public InstanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstanceException(Throwable cause) {
        super(cause);
    }
}
