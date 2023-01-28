package anton.skripin.development.eumcf.service;

import anton.skripin.development.domain.model.Association;
import anton.skripin.development.domain.model.Attribute;
import anton.skripin.development.domain.model.ModelElement;
import anton.skripin.development.mapper.ModelMapper;
import lombok.SneakyThrows;
import modicio.core.Registry;
import modicio.core.TypeHandle;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static anton.skripin.development.eumcf.util.ScalaToJavaMapping.future;
import static anton.skripin.development.eumcf.util.ScalaToJavaMapping.set;

@Service
public class ModelSpaceServiceImpl implements ModelSpaceService {

    private final ModelMapper<modicio.core.ModelElement> modelMapper;

    private final Registry registry;

    public ModelSpaceServiceImpl(ModelMapper<modicio.core.ModelElement> modelMapper, Registry registry) {
        this.modelMapper = modelMapper;
        this.registry = registry;
    }


    @Override
    @SneakyThrows
    public List<ModelElement> getAllModelElements() {
        List<ModelElement> modelElements = new ArrayList<>();
        set(future(registry.getReferences()).get()).forEach(typeHandle -> {
            modelElements.add(modelMapper.mapToModelElement(typeHandle.getModelElement()));
        });
        return modelElements;
    }

    @Override
    @SneakyThrows
    public ModelElement getModelElementByIdAndName(String id, String name) {
        return modelMapper
                .mapToModelElement(future(registry.getType(name, id))
                        .get()
                        .get()
                        .getModelElement());
    }

    @Override
    @SneakyThrows
    public ModelElement addToOpenedModelElement(String from, String to, String currentPath) {
        String fromKey = from.contains(".") ? StringUtils.substringAfter(from, ".") : "";

        ModelElement modelElement = modelMapper
                .mapToModelElement(
                        future(registry.getType(to, modicio.core.ModelElement.REFERENCE_IDENTITY()))
                                .get()
                                .get()
                                .getModelElement()
                );

        for (Attribute attribute: modelElement.getAttributes()) {
            String currentAttributeKey = attribute.getKey();
            if (StringUtils.isBlank(fromKey)) {
                attribute.setKey(String.format("%s.%s", to, currentAttributeKey));
            } else {
                attribute.setKey(String.format("%s.%s.%s", fromKey, to, currentAttributeKey));
            }
            attribute.setPath(String.format("%s.%s", currentPath, attribute.getPath()));
        }

        for (Association association: modelElement.getAssociations()) {
            association.setPath(String.format("%s.%s", currentPath, association.getPath()));
        }

        if (StringUtils.isNotBlank(fromKey)) {
            modelElement.setName(String.format("%s.%s", from, modelElement.getName()));
        } else {
            modelElement.setName(String.format("#.%s", modelElement.getName()));
        }

        return modelElement;
    }

    @Override
    @SneakyThrows
    public Map<String, List<String>> getAllTypesAndTheirSupertypesMap() {
        Map<String, List<String>> allTypesAndSupertypesMap = new HashMap<>();
        for (String type : set(future(registry.getAllTypes()).get())) {
            TypeHandle typeHandle = future(registry.getType(type, modicio.core.ModelElement.REFERENCE_IDENTITY()))
                    .get()
                    .get();
            future(typeHandle.unfold()).get();
            if (!typeHandle.getIsTemplate()) {
                allTypesAndSupertypesMap.put(type, new ArrayList<>(set(typeHandle.getModelElement().getTypeClosure())));
            }
        }
        return allTypesAndSupertypesMap;
    }
}
