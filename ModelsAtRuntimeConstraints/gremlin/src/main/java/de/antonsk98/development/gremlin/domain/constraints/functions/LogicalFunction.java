package de.antonsk98.development.gremlin.domain.constraints.functions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.antonsk98.development.gremlin.service.FunctionName;

import java.util.List;
import java.util.Objects;

public class LogicalFunction extends Function {


    @JsonCreator
    public LogicalFunction(
            @JsonProperty("functionName") FunctionName functionName,
            @JsonProperty("returnType") ReturnType returnType,
            @JsonProperty("nestedFunctions") List<Function> nestedFunctions) {
        super(functionName, returnType, nestedFunctions);
        validateLogicalFunction(nestedFunctions);;
    }

    private void validateLogicalFunction(List<Function> nestedFunctions) {
        if (Objects.isNull(nestedFunctions) || nestedFunctions.size() < 2) {
            throw new IllegalArgumentException("Logical function must contain at least two boolean nested functions");
        }
        if (nestedFunctions.stream().anyMatch(function -> !function.getReturnType().equals(ReturnType.BOOLEAN))) {
            throw new IllegalArgumentException("Nested functions of a logical function must return boolean type");
        }
    }
}