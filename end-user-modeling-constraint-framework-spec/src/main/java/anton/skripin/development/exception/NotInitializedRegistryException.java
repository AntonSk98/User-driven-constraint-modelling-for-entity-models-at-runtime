package anton.skripin.development.exception;

public class NotInitializedRegistryException extends RuntimeException {
    public NotInitializedRegistryException(String registryName) {
        super(String.format("Registry %s must be initialized before use. Use initialize() beforehand!", registryName));
    }
}
