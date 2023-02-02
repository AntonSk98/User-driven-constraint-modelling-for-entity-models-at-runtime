package anton.skripin.development.mapper;

import anton.skripin.development.domain.model.ModelElement;

/**
 * Interfaces that maps two technical model element spaces.
 * On the one side, there is the technical space of an end-user constraint engine.
 * On the other side, there is the technical space of a platform that provides means for modeling and entity management.
 * @param <TargetModelElement> model element of the technical space of a modeling tool
 */
public interface ModelMapper<TargetModelElement> {
    ModelElement mapToModelElement(TargetModelElement sourceModel);
}
