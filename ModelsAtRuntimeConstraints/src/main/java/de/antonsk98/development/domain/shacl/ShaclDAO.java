package de.antonsk98.development.domain.shacl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.jena.rdf.model.Resource;

import java.util.List;

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
    private List<Resource> resourceList;

    /**
     * Adds constraint resource for a function.
     *
     * @param resource {@link Resource}
     */
    public void addResource(Resource resource) {
        this.resourceList.add(resource);
    }

    /**
     * Clears up constraint resource for a function.
     */
    public void clearResourceList() {
        this.resourceList.clear();
    }
}
