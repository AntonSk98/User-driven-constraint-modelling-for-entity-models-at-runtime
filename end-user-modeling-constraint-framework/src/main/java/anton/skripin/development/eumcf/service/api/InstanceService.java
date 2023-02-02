package anton.skripin.development.eumcf.service.api;

import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.domain.instance.Link;
import anton.skripin.development.domain.instance.Slot;

import java.util.List;
import java.util.Map;

public interface InstanceService {

    /**
     * Removes all instances from a registry by a given type.
     *
     * @param type of a model element
     * @return true if instances are removed, false otherwise
     */
    boolean removeInstancesByType(String type);

    /**
     * Removes an instance from a registry by a given uuid.
     *
     * @param uuid of an instance
     * @return true if an instance is removed, false otherwise
     */
    boolean deleteInstanceById(String uuid);

    /**
     * Creates an instance.
     *
     * @param instanceOf type of model element
     * @param slots      list of instantiated attributes
     * @param links      list of instantiated associations
     * @return true if an instance is created
     */
    boolean createInstance(String instanceOf, List<Slot> slots, List<Link> links);

    /**
     * Updates an instance
     *
     * @param uuid  of an instance
     * @param slots list of instantiated attributes
     * @param links list of instantiated associations
     * @return true if an instance is updated
     */
    boolean updateInstance(String uuid, List<Slot> slots, List<Link> links);

    /**
     * Returns an instance by uuid.
     *
     * @param uuid of an instance
     * @return {@link InstanceElement}
     */
    InstanceElement getInstanceByUuid(String uuid);

    /**
     * Return all instances for each type.
     * Example: "[Car: [Honda, BMW, Audi], Engineer: [PersonA, PersonB, PersonC]]"
     *
     * @return map
     */
    Map<String, List<InstanceElement>> getTypeToInstancesMap();

    /**
     * Enriches an {@link InstanceElement} with all associations that are not yet instantiated by links.
     *
     * @param instanceElement to be enriched
     * @return {@link InstanceElement}
     */
    InstanceElement withAssociations(InstanceElement instanceElement);
}
