package anton.skripin.development.eumcf.service.api;

import anton.skripin.development.domain.model.ModelElement;
import anton.skripin.development.mapper.ModelMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ModelService {

    /**
     * Updates attribute definition of a model element.
     *
     * @param uuid          uuid of an attribute
     * @param type          type of model element that holds it
     * @param attributeUuid uuid of an attribute
     * @param key           key of an attribute
     * @param datatype      datatype of an attribute
     * @return true if update operation finished successfully
     */
    boolean updateAttributeDefinition(String uuid, String type, String attributeUuid, String key, String datatype);

    /**
     * Updates association definition of a model element.
     *
     * @param uuid            uuid of an association
     * @param type            type of model element that holds it
     * @param associationUuid uuid of an association
     * @param byRelation      connection link between source and target model elements
     * @param target          target type of model element
     * @return true if update operation finished successfully
     */
    boolean updateAssociationDefinition(String uuid, String type, String associationUuid, String byRelation, String target);

    /**
     * Returns all model elements defined in a registry. The result is then mapped to {@link ModelElement} by {@link ModelMapper <modicio.core.ModelElement>}.
     *
     * @return list of {@link ModelElement}
     */
    List<ModelElement> getAllModelElements();

    /**
     * Retrieves a {@link ModelElement} by its id and name. The result is mapped by {@link ModelMapper<modicio.core.ModelElement>}.
     *
     * @param id   of a model element
     * @param name of a model element
     * @return {@link ModelElement}
     */
    ModelElement getModelElementByIdAndName(String id, String name);

    /**
     * Extends a model with a target model that can be accessed by navigation via path.
     *
     * @param from        model element that is subject to extension
     * @param to          model element that extends the target
     * @param currentPath current path that is used to link two model elements
     * @return extended {@link ModelElement}
     */
    ModelElement addToOpenedModelElement(String from, String to, String currentPath);

    /**
     * Returns a map where key is a type of model element and value is a list of all supertypes of a given type.
     * Example: "SoftwareEngineer, [ITSpecialist, Employee, Person]"
     *
     * @return map
     */
    Map<String, List<String>> getAllTypesAndTheirSupertypesMap();
}
