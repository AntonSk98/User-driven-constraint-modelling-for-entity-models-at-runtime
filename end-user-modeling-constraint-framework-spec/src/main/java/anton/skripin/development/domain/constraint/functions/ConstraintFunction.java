package anton.skripin.development.domain.constraint.functions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Getter
@Setter
@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LogicalFunction.class, name = FunctionType.LOGICAL_FUNCTION),
        @JsonSubTypes.Type(value = StringBasedFunction.class, name = FunctionType.STRING_BASED_FUNCTION)}
)
public abstract class ConstraintFunction {
    private String name;

    @JsonIgnore
    private ConstraintFunction parentFunction;

    public ConstraintFunction(String name) {
        this.name = name;
    }
}
