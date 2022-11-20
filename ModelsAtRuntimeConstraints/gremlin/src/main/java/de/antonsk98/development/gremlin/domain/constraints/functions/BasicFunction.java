package de.antonsk98.development.gremlin.domain.constraints.functions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.antonsk98.development.gremlin.service.FunctionName;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BasicFunction extends Function {
    private final String target;
    private final Map<String, String> params;

    @JsonCreator
    public BasicFunction(
            @JsonProperty("functionName") FunctionName functionName,
            @JsonProperty("target") String target,
            @JsonProperty("params") Map<String, String> params,
            @JsonProperty("returnType") ReturnType returnType) {
        super(functionName, returnType, Collections.emptyList());
        this.target = target;
        this.params = params;
    }

    public String getTarget() {
        return target;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
