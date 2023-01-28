package anton.skripin.development.domain.constraint.functions;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public abstract class HierarchicalConstraintFunction extends ConstraintFunction {
    private List<ConstraintFunction> nestedFunctions;

    public HierarchicalConstraintFunction(String name, List<ConstraintFunction> nestedFunctions) {
        super(name);
        this.nestedFunctions = nestedFunctions;
        if (Objects.isNull(this.nestedFunctions)) {
            this.nestedFunctions = new ArrayList<>();
        } else {
            this.nestedFunctions.forEach(constraintFunction -> constraintFunction.setParentFunction(this));
        }
    }
}
