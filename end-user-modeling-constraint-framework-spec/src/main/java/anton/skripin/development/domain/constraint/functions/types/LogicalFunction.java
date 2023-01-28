package anton.skripin.development.domain.constraint.functions.types;

import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import anton.skripin.development.domain.constraint.functions.HierarchicalConstraintFunction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LogicalFunction extends HierarchicalConstraintFunction {

    @JsonCreator
    public LogicalFunction(
            @JsonProperty("name") String name,
            @JsonProperty("nestedFunctions") List<ConstraintFunction> nestedFunctions) {
        super(name, nestedFunctions);
    }
}
