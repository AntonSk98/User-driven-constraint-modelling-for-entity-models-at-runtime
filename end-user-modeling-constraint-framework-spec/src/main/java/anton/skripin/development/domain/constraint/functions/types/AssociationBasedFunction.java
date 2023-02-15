package anton.skripin.development.domain.constraint.functions.types;

import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

/**
 * Association-based function.
 */
@Getter
public class AssociationBasedFunction extends ConstraintFunction {

    private final Map<String, String> params;

    /**
     * Constructor.
     *
     * @param name   of a constraint
     * @param params params of a function
     */
    @JsonCreator
    public AssociationBasedFunction(
            @JsonProperty("name") String name,
            @JsonProperty("params") Map<String, String> params) {
        super(name);
        this.params = params;
    }

    @Override
    public Optional<Map<String, String>> params() {
        return Optional.of(params);
    }
}
