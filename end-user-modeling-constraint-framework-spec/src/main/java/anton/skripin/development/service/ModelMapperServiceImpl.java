package anton.skripin.development.service;

import anton.skripin.development.domain.model.ModelElement;
import anton.skripin.development.mapper.ModelMapper;
import anton.skripin.development.registry.ModelMapperRegistry;
import anton.skripin.development.service.api.ModelMapperService;

public class ModelMapperServiceImpl<TargetModelElement> implements ModelMapperService<TargetModelElement> {

    private final ModelMapperRegistry<TargetModelElement> modelMapperRegistry;

    public ModelMapperServiceImpl(ModelMapper<TargetModelElement> modelMapper) {
        this.modelMapperRegistry = new ModelMapperRegistry<>(modelMapper);
    }

    @Override
    public ModelElement mapToModelElement(TargetModelElement targetModel) {
        return this.modelMapperRegistry.mapToModelElement(targetModel);
    }
}
