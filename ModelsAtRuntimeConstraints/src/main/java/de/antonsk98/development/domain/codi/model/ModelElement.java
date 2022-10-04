package de.antonsk98.development.domain.codi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Domain of a single model element.
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
    private Set<Constraint> constraints;

    /**
     * Getter for {@link #constraints}.
     * @return {@link #constraints}
     */
    public Set<Constraint> getConstraints() {
        if (Objects.isNull(constraints)) {
            this.constraints = new HashSet<>();
        }
        return this.constraints;
    }
}
