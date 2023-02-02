package anton.skripin.development.exception;

/**
 * Concrete {@link ConstraintException} thrown if a function with a given name already exists.
 */
public class UniqueFunctionNameException extends ConstraintException {
    public UniqueFunctionNameException(String functionName) {
        super(String.format("Function '%s' exists in the scope of templates and no duplicates are allowed!", functionName));
    }
}
