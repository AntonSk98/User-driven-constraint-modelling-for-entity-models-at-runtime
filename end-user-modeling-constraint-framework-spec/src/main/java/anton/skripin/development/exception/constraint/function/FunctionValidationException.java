package anton.skripin.development.exception.constraint.function;

import anton.skripin.development.domain.ValidationUtils;

/**
 * Exception being thrown during validation operations.
 * E.g. during operations that are defined in {@link ValidationUtils}
 */
public class FunctionValidationException extends FunctionException {

    public FunctionValidationException(String message) {
        super(message);
    }

    public FunctionValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunctionValidationException(Throwable cause) {
        super(cause);
    }
}
