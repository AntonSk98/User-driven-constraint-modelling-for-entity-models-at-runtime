package anton.skripin.development.domain.constraint;

import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Constraint {
    private String uuid;
    private String name;
    private String targetModelElementId;
    private String targetModelElementName;
    private String violationMessage;
    private ViolationLevel violationLevel;
    private ConstraintFunction constraintFunction;
}
