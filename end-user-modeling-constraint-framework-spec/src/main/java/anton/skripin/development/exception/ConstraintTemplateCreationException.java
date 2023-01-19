package anton.skripin.development.exception;

public class ConstraintTemplateCreationException extends RuntimeException {

    public ConstraintTemplateCreationException() {
        super("Failed to construct a template for a constraint");
    }

    public ConstraintTemplateCreationException(String name) {
        super(String.format("Failed to construct template for method %s", name));
    }
}
