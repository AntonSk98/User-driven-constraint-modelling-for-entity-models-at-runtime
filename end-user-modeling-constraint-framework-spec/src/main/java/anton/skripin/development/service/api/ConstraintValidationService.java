package anton.skripin.development.service.api;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.ConstraintValidationReport;
import anton.skripin.development.domain.instance.InstanceElement;

import java.util.List;
import java.util.Set;

/**
 * Provides classes needed to validate a constraint.
 */
public interface ConstraintValidationService {

    /**
     * Returns necessary elements that must be present in an instance graph to evaluate a constraint.
     * @param constraint is used to analyze required depth and element of an instance graph
     * @return required subgraph elements
     */

    Set<Set<String>> getRequiredSubgraphElements(Constraint constraint);

    /**
     * Validates a constraint.
     * @param uuid of an element that should be evaluated against constraint conformance
     * @param subgraphForValidation subgraph of instance data and relations
     * @param constraint {@link Constraint}
     * @return validation report
     */
    ConstraintValidationReport validateConstraint(String uuid, List<InstanceElement> subgraphForValidation, Constraint constraint);
}
