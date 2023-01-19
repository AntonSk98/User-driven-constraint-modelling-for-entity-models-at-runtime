package anton.skripin.development.mapper;

import anton.skripin.development.domain.model.ModelElement;

public class SimpleModelMapper implements ModelMapper<ModelElement> {
    @Override
    public ModelElement mapToModelElement(ModelElement sourceModel) {
        return sourceModel;
    }
}
