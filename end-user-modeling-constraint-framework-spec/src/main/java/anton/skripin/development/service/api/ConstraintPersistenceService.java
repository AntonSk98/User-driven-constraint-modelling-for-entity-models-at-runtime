package anton.skripin.development.service.api;

import anton.skripin.development.domain.constraint.Constraint;

import java.util.List;
import java.util.Map;

public interface ConstraintPersistenceService {
    boolean saveConstraint(String id, Constraint constraint);

    List<Constraint> getAllConstraints(String type);

    List<Constraint> getAllConstraintsByName(String type, String name);

    Constraint getConstraintByUuid(String uuid);

    Map<String, List<Constraint>> getGroupConstraintsByTypeAnsSupertypes(Map<String, List<String>> typeToSupertypeMap);

    boolean removeConstraintByUuid(String uuid);
}
