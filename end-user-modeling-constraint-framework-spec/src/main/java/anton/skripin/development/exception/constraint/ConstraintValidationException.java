package anton.skripin.development.exception.constraint;

/**
 * Exception thrown during constraint validation.
 */
public class ConstraintValidationException extends RuntimeException {
    public ConstraintValidationException(String message) {
        super(message);
    }

    public ConstraintValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstraintValidationException(Throwable cause) {
        super(cause);
    }
}
