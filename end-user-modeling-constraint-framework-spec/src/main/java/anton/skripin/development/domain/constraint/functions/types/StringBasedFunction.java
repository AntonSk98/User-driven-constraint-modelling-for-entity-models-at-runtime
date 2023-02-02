package anton.skripin.development.domain.constraint.functions.types;

import anton.skripin.development.domain.constraint.functions.ContextConstraintFunction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Specifies restrictions applied to attributes with a string representation.
 */
public class StringBasedFunction extends ContextConstraintFunction {

    /**
     * Constructor.
     * @param name of a constraint
     * @param path of an attribute
     * @param params params of a function
     */
    @JsonCreator
    public StringBasedFunction(
            @JsonProperty("name") String name,
            @JsonProperty("path") String path,
            @JsonProperty("params") Map<String, String> params) {
        super(name, path, params);
    }
}
