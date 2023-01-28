package anton.skripin.development.service.api;

import anton.skripin.development.domain.constraint.Constraint;

import java.util.List;
import java.util.Map;

public interface ConstraintPersistenceService {

    boolean saveConstraint(Constraint constraint);

    List<Constraint> getAllConstraints();

    List<Constraint> getAllConstraintsByName(String name);

    List<Constraint> getAllConstraintsByType(String type);

    Constraint getConstraintByUuid(String uuid);

    Map<String, List<Constraint>> getGroupedConstraintsByName();

    Map<String, List<Constraint>> getGroupConstraintsByTypeAnsSupertypes(Map<String, List<String>> typeToSupertypeMap);

    boolean removeConstraintByUuid(String uuid);
}
