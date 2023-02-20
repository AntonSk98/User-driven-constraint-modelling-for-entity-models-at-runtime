package anton.skripin.development.exception.constraint;

/**
 * Exception related to constraint evaluation.
 */
public class GraphConstraintException extends RuntimeException {
    public GraphConstraintException(String message) {
        super(message);
    }

    public GraphConstraintException(String message, Throwable cause) {
        super(message, cause);
    }

    public GraphConstraintException(Throwable cause) {
        super(cause);
    }
}
