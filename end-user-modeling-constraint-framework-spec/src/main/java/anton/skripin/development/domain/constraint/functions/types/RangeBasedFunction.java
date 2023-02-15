package anton.skripin.development.domain.constraint.functions.types;

import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

import static anton.skripin.development.domain.ValidationUtils.validateAttribute;

/**
 * Limits the set of all possible values to be within the specified range depending on the operation.
 */
@Getter
public class RangeBasedFunction extends ConstraintFunction {

    private final String attribute;
    private final Map<String, String> params;

    @JsonCreator
    public RangeBasedFunction(
            @JsonProperty("name") String name,
            @JsonProperty("attribute") String attribute,
            @JsonProperty("params") Map<String, String> params) {
        super(name);
        validateAttribute(attribute);
        this.attribute = attribute;
        this.params = params;
    }

    public RangeBasedFunction(
            String name,
            String attribute,
            Map<String, String> params,
            boolean asTemplate
    ) {
        super(name);
        if (!asTemplate) {
            validateAttribute(attribute);
        }
        this.attribute = attribute;
        this.params = params;
    }

    @Override
    public Optional<String> attribute() {
        return Optional.of(attribute);
    }

    @Override
    public Optional<Map<String, String>> params() {
        return Optional.of(params);
    }
}
