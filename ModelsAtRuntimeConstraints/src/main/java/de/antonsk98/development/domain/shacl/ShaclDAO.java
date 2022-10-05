package de.antonsk98.development.domain.shacl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.jena.rdf.model.Resource;

import java.util.*;

/**
 * Data access object used to transform codi model into shacl constraint model.
 *
 * @author Anton Skripin
 */
@Getter
@AllArgsConstructor
public class ShaclDAO {
    private DeepModel model;
    private Resource constraintResource;
    private MultiValuedMap<String, Resource> resourceMap;

    /**
     * Adds constraint resource for a function.
     *
     * @param resource {@link Resource}
     */
    public void addResource(String parentId, Resource resource) {
        resourceMap.put(parentId, resource);
    }

    /**
     * Returns a collection of all associated resources with the given parent id.
     * @param parentId parent id
     * @return collection of all associated resources
     */
    public Collection<Resource> getResourcesByParentId(String parentId) {
        return resourceMap.get(parentId);
    }

    /**
     * Clears up constraint resource for a function.
     */
    public void clearResourceList() {
        this.resourceMap.clear();
    }

    /**
     * Deletes all resources associated with the given parent id.
     * @param parentId parent id
     */
    public void deleteAllResourcesByParentId(String parentId) {
        this.resourceMap.remove(parentId);
    }
}
