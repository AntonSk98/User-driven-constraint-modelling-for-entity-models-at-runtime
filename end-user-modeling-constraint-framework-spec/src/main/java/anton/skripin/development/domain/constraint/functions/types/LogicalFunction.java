package anton.skripin.development.domain.constraint.functions.types;

import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import anton.skripin.development.domain.constraint.functions.HierarchicalConstraintFunction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Encapsulates multiple nested constraints grouped by a logical operator.
 * The overall constraint conformance is evaluated among all nested constraints.
 * Its result depends on a logical operation (and, or).
 */
public class LogicalFunction extends HierarchicalConstraintFunction {

    /**
     * Constructor.
     * @param name name of a constraint
     * @param nestedFunctions list of nested functions
     */
    @JsonCreator
    public LogicalFunction(
            @JsonProperty("name") String name,
            @JsonProperty("nestedFunctions") List<ConstraintFunction> nestedFunctions) {
        super(name, nestedFunctions);
    }
}
