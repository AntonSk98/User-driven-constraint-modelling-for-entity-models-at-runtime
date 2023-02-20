package ansk.development.exception.constraint;

/**
 * Exception related to abstract to platform-specific graph transformation.
 */
public class GraphTransformationException extends RuntimeException {
    public GraphTransformationException(String message) {
        super(message);
    }

    public GraphTransformationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GraphTransformationException(Throwable cause) {
        super(cause);
    }
}
