package de.antonsk98.development.domain.shacl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import java.util.List;

/**
 * Container used to transform codi model into shacl shape graph.
 *
 * @author Anton Skripin
 */
@Getter
@AllArgsConstructor
public class ShaclContainer {
    private Model model;
    private Resource constraintResource;
    private List<Resource> resourceList;

    /**
     * Adds constraint resource for a function.
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
