package anton.skripin.development.domain.template;

import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Template for providing a {@link ConstraintFunction}.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectTemplatePlaceholder extends ConstraintFunction {

    private final String key;

    private final String value;

    public ObjectTemplatePlaceholder(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Optional<String> attribute() {
        throw new UnsupportedOperationException("This function must be used only internally");
    }

    @Override
    public Optional<String> navigation() {
        throw new UnsupportedOperationException("This function must be used only internally");
    }

    @Override
    public Optional<Map<String, String>> params() {
        throw new UnsupportedOperationException("This function must be used only internally");
    }

    @Override
    public Optional<List<ConstraintFunction>> booleanFunctions() {
        throw new UnsupportedOperationException("This function must be used only internally");
    }

    @Override
    public Optional<ConstraintFunction> lambdaFunction() {
        throw new UnsupportedOperationException("This function must be used only internally");
    }
}
