package anton.skripin.development.exception;

public class UniqueFunctionNameExcpetion extends RuntimeException {
    public UniqueFunctionNameExcpetion(String functionName) {
        super(String.format("Function '%s' exists in the scope of templates and no duplicates are allowed!", functionName));
    }
}
