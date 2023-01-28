package anton.skripin.development.domain.constraint.functions.types;

import anton.skripin.development.domain.constraint.functions.ContextConstraintFunction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class StringBasedFunction extends ContextConstraintFunction {

    @JsonCreator
    public StringBasedFunction(
            @JsonProperty("name") String name,
            @JsonProperty("path") String path,
            @JsonProperty("params") Map<String, String> params) {
        super(name, path, params);
    }
}
