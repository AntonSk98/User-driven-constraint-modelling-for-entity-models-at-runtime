package anton.skripin.development.registry;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.mapper.AbstractToPSConstraintMapper;

public class AbstractToPSConstraintMapperRegistry {

    private final AbstractToPSConstraintMapper abstractToPSConstraintMapper;

    public AbstractToPSConstraintMapperRegistry(AbstractToPSConstraintMapper abstractToPSConstraintMapper) {
        this.abstractToPSConstraintMapper = abstractToPSConstraintMapper;
    }

    public String mapToPlatformSpecifConstraint(Constraint constraint) {
        return this.abstractToPSConstraintMapper.mapToPlatformSpecificConstraint(constraint);
    }
}
