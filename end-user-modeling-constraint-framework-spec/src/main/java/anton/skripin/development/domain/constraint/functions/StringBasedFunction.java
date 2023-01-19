package anton.skripin.development.domain.constraint.functions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class StringBasedFunction extends ContextConstraintFunction {

    @JsonCreator
    public StringBasedFunction(
            @JsonProperty("name") String name,
            @JsonProperty("targetId") String targetId,
            @JsonProperty("targetField") String targetField,
            @JsonProperty("params") Map<String, String> params) {
        super(name, targetId, targetField, params);
    }
}
