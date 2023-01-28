package anton.skripin.development.eumcf.service;

import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.mapper.InstanceMapper;
import lombok.SneakyThrows;
import modicio.core.DeepInstance;
import modicio.core.Plugin;
import modicio.core.Registry;
import modicio.core.datamappings.PluginData;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static anton.skripin.development.eumcf.util.ScalaToJavaMapping.future;
import static anton.skripin.development.eumcf.util.ScalaToJavaMapping.set;


@Service
public class InstanceSpaceServiceImpl implements InstanceSpaceService {

    private final Registry registry;

    private final InstanceMapper<DeepInstance> modicioInstanceMapper;

    public InstanceSpaceServiceImpl(Registry registry, InstanceMapper<DeepInstance> modicioInstanceMapper) {
        this.registry = registry;
        this.modicioInstanceMapper = modicioInstanceMapper;
    }

    @Override
    @SneakyThrows
    public Map<String, List<InstanceElement>> getTypeToInstancesMap() {
        Map<String, List<InstanceElement>> instanceElementMap = new HashMap<>();

        set(future(registry.getAllTypes()).get()).forEach(type -> {
            try {
                List<InstanceElement> elements = set(future(registry.getAll(type)).get())
                        .stream()
                        .filter(deepInstance -> !deepInstance.getTypeHandle().getIsTemplate())
                        .map(modicioInstanceMapper::mapToInstanceElement)
                        .toList();
                if (!elements.isEmpty()) {
                    instanceElementMap.put(type, elements);
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        return instanceElementMap;
    }

    @Override
    @SneakyThrows
    public InstanceElement getInstanceById(String uuid) {
        DeepInstance deepInstance = future(registry.get(uuid)).get().get();
        future(deepInstance.unfold()).get();
        return modicioInstanceMapper.mapToInstanceElement(deepInstance);
    }
}
