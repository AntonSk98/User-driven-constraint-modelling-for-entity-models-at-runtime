package anton.skripin.development.domain.constraint.functions;

import anton.skripin.development.domain.constraint.functions.types.LogicalFunction;
import anton.skripin.development.domain.constraint.functions.types.StringBasedFunction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

/**
 * Abstract class representing a function
 */
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

    /**
     * Name of a function
     *
     * @param name name
     */
    public ConstraintFunction(String name) {
        this.name = name;
    }

    /**
     * Parses a function to {@link HierarchicalConstraintFunction}.
     *
     * @param constraintFunction {@link ConstraintFunction}
     * @return {@link HierarchicalConstraintFunction}
     */
    public static HierarchicalConstraintFunction toHierarchicalFunction(ConstraintFunction constraintFunction) {
        return (HierarchicalConstraintFunction) constraintFunction;
    }

    public static ContextConstraintFunction toContextConstraintFunction(ConstraintFunction constraintFunction) {
        return (ContextConstraintFunction) constraintFunction;
    }

    /**
     * Returns true if a function is {@link HierarchicalConstraintFunction}.
     *
     * @return true or false
     */
    @JsonIgnore
    public boolean isHierarchicalFunction() {
        return this instanceof HierarchicalConstraintFunction;
    }

    /**
     * Returns true if a function is {@link ContextConstraintFunction}.
     *
     * @return true or false
     */
    @JsonIgnore
    public boolean isContextConstraintFunction() {
        return this instanceof ContextConstraintFunction;
    }
}
