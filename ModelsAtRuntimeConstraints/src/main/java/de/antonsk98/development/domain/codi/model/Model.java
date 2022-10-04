package de.antonsk98.development.domain.codi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Domain of a model-space containing a set of {@link ModelElement}.
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
