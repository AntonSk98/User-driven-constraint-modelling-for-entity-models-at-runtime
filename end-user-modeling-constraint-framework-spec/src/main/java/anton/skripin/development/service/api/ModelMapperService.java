package anton.skripin.development.service.api;

import anton.skripin.development.domain.model.ModelElement;

public interface ModelMapperService<TargetModelElement> {

    ModelElement mapToModelElement(TargetModelElement targetModel);
}
