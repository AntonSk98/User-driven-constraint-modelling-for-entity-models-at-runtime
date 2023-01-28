package anton.skripin.development.service.api;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.instance.InstanceElement;

import java.util.List;
import java.util.Set;

public interface ConstraintValidationService {

    Set<List<String>> getRequiredSubgraphElements(Constraint constraint);

    Object validateConstraint(List<InstanceElement> subgraphForValidation, Constraint constraint);
}
