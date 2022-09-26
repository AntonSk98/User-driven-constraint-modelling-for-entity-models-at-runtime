package de.antonsk98.development.domain.codi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Container for a model element.
 *
 * @author Anton Skripin.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModelElement {
    private String name;
    private boolean template;
    private Set<String> childOf;
    private Set<String> associations;
    private Set<String> attributes;
    private Set<String> values;
}
