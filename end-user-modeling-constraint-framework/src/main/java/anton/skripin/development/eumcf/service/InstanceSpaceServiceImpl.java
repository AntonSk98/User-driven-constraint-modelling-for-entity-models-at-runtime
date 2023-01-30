package anton.skripin.development.eumcf.service;

import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.domain.instance.Link;
import anton.skripin.development.mapper.InstanceMapper;
import lombok.SneakyThrows;
import modicio.core.DeepInstance;
import modicio.core.Registry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static anton.skripin.development.eumcf.util.ScalaToJavaMapping.*;


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

    @Override
    @SneakyThrows
    public InstanceElement withAssociations(InstanceElement instanceElement) {
        DeepInstance deepInstance = future(registry.get(instanceElement.getUuid())).get().get();
        future(deepInstance.unfold()).get();
        List<Link> listOfLinks = new ArrayList(instanceElement.getLinks());
        listOfLinks.addAll(map(deepInstance.associationRuleMap())
                .keySet()
                .stream()
                .filter(association -> instanceElement.getLinks().stream().noneMatch(link -> link.getName().equals(association)))
                .map(association -> {
                    Link link = new Link();
                    link.setName(association);
                    return link;
                })
                .toList());
        instanceElement.setLinks(listOfLinks);
        System.out.println();
        return instanceElement;
    }
}
