package anton.skripin.development.exception;

public class NoTemplateFoundException extends RuntimeException {

    public NoTemplateFoundException(String message) {
        super(String.format("Function with name %s does not exist in registry. Consider adding it first!"));
    }
}
