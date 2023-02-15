package anton.skripin.development.domain.constraint.functions.types;

import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Encapsulates multiple nested constraints grouped by a logical operator.
 * The overall constraint conformance is evaluated among all nested constraints.
 * Its result depends on a logical operation (and, or).
 */
@Getter
public class LogicalFunction extends ConstraintFunction {

    private final List<ConstraintFunction> booleanFunctions;

    /**
     * Constructor.
     *
     * @param name            name of a constraint
     * @param nestedFunctions list of nested functions
     */
    @JsonCreator
    public LogicalFunction(
            @JsonProperty("name") String name,
            @JsonProperty("booleanFunctions") List<ConstraintFunction> nestedFunctions) {
        super(name);
        assert nestedFunctions != null && nestedFunctions.size() >= 2;
        this.booleanFunctions = nestedFunctions;
        this.booleanFunctions.forEach(constraintFunction -> constraintFunction.setParentFunction(this));
    }

    @Override
    public Optional<List<ConstraintFunction>> booleanFunctions() {
        return Optional.of(booleanFunctions);
    }


}
