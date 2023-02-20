package ansk.development.mapper;

import ansk.development.domain.instance.InstanceElement;

/**
 * Simple implementation of {@link InstanceMapper}.
 * It should not be used in production until required or necessary
 */
public class SimpleInstanceMapper implements InstanceMapper<InstanceElement> {

    /**
     * Maps {@link InstanceElement} to itself.
     * This mapper should be used only for testing purposes or fast bootstrap.
     * @param instanceElement {@link InstanceElement}
     * @return {@link InstanceElement}
     */
    @Override
    public InstanceElement mapToInstanceElement(InstanceElement instanceElement) {
        return instanceElement;
    }
}
