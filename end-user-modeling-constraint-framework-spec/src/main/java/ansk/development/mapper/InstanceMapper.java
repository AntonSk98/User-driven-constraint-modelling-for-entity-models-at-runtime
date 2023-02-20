package ansk.development.mapper;

import ansk.development.domain.instance.InstanceElement;

/**
 * Interfaces that maps two technical instance element spaces.
 * On the one side, there is the technical space of an end-user constraint engine.
 * On the other side, there is the technical space of a platform that provides means for modeling and entity management.
 * @param <TargetInstanceElement> instance element of the technical space of a modeling tool
 */
public interface InstanceMapper <TargetInstanceElement> {
    InstanceElement mapToInstanceElement(TargetInstanceElement targetInstanceElement);
}
