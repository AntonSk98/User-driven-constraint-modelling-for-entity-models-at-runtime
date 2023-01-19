package anton.skripin.development.domain.constraint.functions;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public abstract class HierarchicalConstraintFunction extends ConstraintFunction {
    private List<ConstraintFunction> nestedFunction;

    public HierarchicalConstraintFunction(String name, List<ConstraintFunction> nestedFunction) {
        super(name);
        this.nestedFunction = nestedFunction;
        if (Objects.isNull(this.nestedFunction)) {
            this.nestedFunction = new ArrayList<>();
        } else {
            this.nestedFunction.forEach(constraintFunction -> constraintFunction.setParentFunction(this));
        }
    }
}
