package anton.skripin.development.eumcf.service;

import anton.skripin.development.domain.instance.InstanceElement;

import java.util.List;
import java.util.Map;

public interface InstanceSpaceService {
    Map<String, List<InstanceElement>> getTypeToInstancesMap();

    InstanceElement getInstanceById(String uuid);

    InstanceElement withAssociations(InstanceElement instanceElement);
}
