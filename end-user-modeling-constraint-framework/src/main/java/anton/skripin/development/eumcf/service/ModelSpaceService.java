package anton.skripin.development.eumcf.service;


import anton.skripin.development.domain.model.ModelElement;

import java.util.List;
import java.util.Map;

public interface ModelSpaceService {
    List<ModelElement> getAllModelElements();

    ModelElement getModelElementByIdAndName(String id, String name);

    ModelElement addToOpenedModelElement(String from, String to, String currentPath);

    Map<String, List<String>> getAllTypesAndTheirSupertypesMap();


}
