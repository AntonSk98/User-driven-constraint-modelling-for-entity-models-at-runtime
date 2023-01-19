package anton.skripin.development.service.api;

import anton.skripin.development.domain.instance.InstanceElement;

public interface InstanceMapperService<TargetInstanceElement> {
    InstanceElement mapToInstanceElement(TargetInstanceElement instanceElement);
}
