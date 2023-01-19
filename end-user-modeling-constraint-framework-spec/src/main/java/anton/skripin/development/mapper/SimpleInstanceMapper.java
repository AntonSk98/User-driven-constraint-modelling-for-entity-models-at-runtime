package anton.skripin.development.mapper;

import anton.skripin.development.domain.instance.InstanceElement;

public class SimpleInstanceMapper implements InstanceMapper<InstanceElement> {
    @Override
    public InstanceElement mapToInstanceElement(InstanceElement instanceElement) {
        return instanceElement;
    }
}
