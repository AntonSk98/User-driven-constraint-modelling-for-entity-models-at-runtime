package anton.skripin.development.exception;

public class ConstraintParsingException extends RuntimeException {
    public ConstraintParsingException(String uuid) {
        super(String.format("Failed to deserialize user-defined constraint from template with id %s", uuid));
    }
}
