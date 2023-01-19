package anton.skripin.development.service;

import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.mapper.InstanceMapper;
import anton.skripin.development.registry.InstanceMapperRegistry;
import anton.skripin.development.service.api.InstanceMapperService;

public class InstanceMapperServiceImpl <TargetInstanceElement> implements InstanceMapperService<TargetInstanceElement> {

    private final InstanceMapperRegistry<TargetInstanceElement> instanceMapperRegistry;

    public InstanceMapperServiceImpl(InstanceMapper<TargetInstanceElement> instanceMapper) {
        this.instanceMapperRegistry = new InstanceMapperRegistry<>(instanceMapper);
    }

    @Override
    public InstanceElement mapToInstanceElement(TargetInstanceElement instanceElement) {
        return this.instanceMapperRegistry.mapToInstanceElement(instanceElement);
    }
}
