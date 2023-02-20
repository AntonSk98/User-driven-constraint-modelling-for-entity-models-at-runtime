package ansk.development.mapper;

import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.instance.InstanceElement;

import java.util.List;

/**
 * Interface that must be implemented by a specific constraint engine.
 * It maps the abstract syntax definition of a {@link Constraint} to a platform specific one.
 */
public interface AbstractToPSConstraintMapper <PlatformSpecificGraph, PlatformSpecificConstraint> {
    PlatformSpecificConstraint mapToPlatformSpecificConstraint(String uuid, Constraint constraint);

    PlatformSpecificGraph mapToPlatformSpecificGraph(List<InstanceElement> subgraphForValidation);
}
