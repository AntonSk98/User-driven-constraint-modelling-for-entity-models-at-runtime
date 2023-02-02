package anton.skripin.development.mapper;

import anton.skripin.development.domain.constraint.Constraint;

/**
 * Interface that must be implemented by a specific constraint engine.
 * It maps the abstract syntax definition of a {@link Constraint} to a platform specific one.
 */
public interface AbstractToPSConstraintMapper {
    String mapToPlatformSpecificConstraint(Constraint constraint);
}
