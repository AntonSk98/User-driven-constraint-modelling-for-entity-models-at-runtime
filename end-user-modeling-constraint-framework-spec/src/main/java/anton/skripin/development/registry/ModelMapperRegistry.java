package anton.skripin.development.registry;

import anton.skripin.development.domain.model.ModelElement;
import anton.skripin.development.exception.NotInitializedRegistryException;
import anton.skripin.development.mapper.ModelMapper;

import java.util.Objects;

public class ModelMapperRegistry<TargetModelElement> {

    private final ModelMapper<TargetModelElement> modelMapper;

    public ModelMapperRegistry(ModelMapper<TargetModelElement> mapper) {
        this.modelMapper = mapper;
    }

    public ModelElement mapToModelElement(TargetModelElement sourceModel) {
        return modelMapper.mapToModelElement(sourceModel);
    }
}
