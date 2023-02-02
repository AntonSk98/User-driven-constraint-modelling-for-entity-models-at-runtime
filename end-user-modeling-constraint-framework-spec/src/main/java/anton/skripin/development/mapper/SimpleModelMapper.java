package anton.skripin.development.mapper;

import anton.skripin.development.domain.model.ModelElement;

public class SimpleModelMapper implements ModelMapper<ModelElement> {

    /**
     * Maps {@link ModelElement} to itself.
     * This mapper is for testing purposes only or fast bootstrap.
     *
     * @param sourceModel {@link ModelElement}
     * @return {@link ModelElement}
     */
    @Override
    public ModelElement mapToModelElement(ModelElement sourceModel) {
        return sourceModel;
    }
}
