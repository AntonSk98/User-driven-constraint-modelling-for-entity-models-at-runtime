package anton.skripin.development.exception;

/**
 * Concrete {@link ConstraintException} thrown in case of template creation failures.
 */
public class ConstraintTemplateCreationException extends ConstraintException {

    public ConstraintTemplateCreationException() {
        super("Failed to construct a template for a constraint");
    }

    public ConstraintTemplateCreationException(String name) {
        super(String.format("Failed to construct template for method %s", name));
    }
}
