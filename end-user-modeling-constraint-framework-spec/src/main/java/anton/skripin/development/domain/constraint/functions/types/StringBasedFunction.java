package anton.skripin.development.domain.constraint.functions.types;

import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static anton.skripin.development.domain.ValidationUtils.validateAttribute;

/**
 * Specifies restrictions applied to attributes with a string representation.
 */
@Getter
public class StringBasedFunction extends ConstraintFunction {

    private final String attribute;
    private final Map<String, String> params;

    /**
     * Constructor.
     *
     * @param name      of a constraint
     * @param attribute of an attribute
     * @param params    params of a function
     */
    @JsonCreator
    public StringBasedFunction(
            @JsonProperty("name") String name,
            @JsonProperty("attribute") String attribute,
            @JsonProperty("params") Map<String, String> params) {
        super(name);
        validateAttribute(attribute);
        this.attribute = attribute;
        this.params = params;
    }

    public StringBasedFunction(
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
        return Optional.ofNullable(params);
    }
}
