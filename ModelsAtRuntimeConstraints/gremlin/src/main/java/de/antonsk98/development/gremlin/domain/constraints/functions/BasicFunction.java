package de.antonsk98.development.gremlin.domain.constraints.functions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BasicFunction extends Function {
    private final String target;
    private final List<Param> params;

    @JsonCreator
    public BasicFunction(
            @JsonProperty("functionName") String functionName,
            @JsonProperty("target") String target,
            @JsonProperty("params") List<Param> params,
            @JsonProperty("returnType") ReturnType returnType) {
        super(functionName, returnType);
        this.target = target;
        this.params = params;
    }

    public String getTarget() {
        return target;
    }

    public List<Param> getParams() {
        return params;
    }
}
