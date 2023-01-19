package anton.skripin.development.domain.constraint.functions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LogicalFunction extends HierarchicalConstraintFunction {

    @JsonCreator
    public LogicalFunction(
            @JsonProperty("name") String name,
            @JsonProperty("nestedFunction") List<ConstraintFunction> nestedFunction) {
        super(name, nestedFunction);
    }
}
