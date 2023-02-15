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
 * Function that is to be created at runtime by end-user.
 */
@Getter
public class RuntimeFunction extends ConstraintFunction {

    private String runtimeFunction;

    @JsonCreator
    public RuntimeFunction(
            @JsonProperty("name") String name,
            @JsonProperty("runtimeFunction") String runtimeFunction) {
        super(name);
        this.runtimeFunction = runtimeFunction;
    }

    @Override
    public Optional<String> runtimeFunction() {
        return Optional.of(runtimeFunction);
    }
}
