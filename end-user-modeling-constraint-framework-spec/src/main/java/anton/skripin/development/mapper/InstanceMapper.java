package anton.skripin.development.mapper;

import anton.skripin.development.domain.instance.InstanceElement;

public interface InstanceMapper <TargetInstanceElement> {
    InstanceElement mapToInstanceElement(TargetInstanceElement targetInstanceElement);
}
