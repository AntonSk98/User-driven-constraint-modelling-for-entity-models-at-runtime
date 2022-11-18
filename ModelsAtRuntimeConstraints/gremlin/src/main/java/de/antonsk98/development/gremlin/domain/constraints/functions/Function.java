package de.antonsk98.development.gremlin.domain.constraints.functions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LogicalFunction.class, name = FunctionTypes.LOGICAL_FUNCTION),
        @JsonSubTypes.Type(value = BasicFunction.class, name = FunctionTypes.BASIC_FUNCTION)
})
public abstract class Function {
    private final String functionName;

    @NotNull
    private final ReturnType returnType;

    public Function(String functionName, @NotNull(value = "Every function must have valid return type!") ReturnType returnType) {
        this.functionName = functionName;
        this.returnType = returnType;
    }

    public String getFunctionName() {
        return functionName;
    }

    public ReturnType getReturnType() {
        return returnType;
    }
}
