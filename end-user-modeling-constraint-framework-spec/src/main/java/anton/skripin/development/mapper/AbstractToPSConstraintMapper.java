package anton.skripin.development.mapper;

import anton.skripin.development.domain.constraint.Constraint;

public interface AbstractToPSConstraintMapper {
    String mapToPlatformSpecificConstraint(Constraint constraint);
}
