package anton.skripin.development.registry;

import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.mapper.InstanceMapper;

public final class InstanceMapperRegistry<TargetInstanceElement> {

    private final InstanceMapper<TargetInstanceElement> instanceMapper;

    public InstanceMapperRegistry(InstanceMapper<TargetInstanceElement> instanceMapper) {
        this.instanceMapper = instanceMapper;
    }

    public InstanceElement mapToInstanceElement(TargetInstanceElement targetInstanceElement) {
        return instanceMapper.mapToInstanceElement(targetInstanceElement);
    }
}
