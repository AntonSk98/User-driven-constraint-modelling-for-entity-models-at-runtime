package anton.skripin.development.exception.constraint.function;

/**
 * General exception related to failed function operations.
 */
public class FunctionException extends RuntimeException {

    public FunctionException(String message) {
        super(message);
    }

    public FunctionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunctionException(Throwable cause) {
        super(cause);
    }
}
