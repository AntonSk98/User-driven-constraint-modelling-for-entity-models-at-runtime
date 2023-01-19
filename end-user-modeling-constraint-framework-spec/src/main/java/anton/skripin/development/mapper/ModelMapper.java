package anton.skripin.development.mapper;

import anton.skripin.development.domain.model.ModelElement;

public interface ModelMapper<TargetModelElement> {
    ModelElement mapToModelElement(TargetModelElement sourceModel);
}
