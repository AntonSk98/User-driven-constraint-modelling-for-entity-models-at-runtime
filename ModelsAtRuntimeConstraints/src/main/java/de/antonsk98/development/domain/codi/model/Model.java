package de.antonsk98.development.domain.codi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Container for a model-space of the deep-instance hierarchy containing a set of model elements.
 *
 * @author Anton Skripin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Model {
    private Set<ModelElement> modelElement;
}
