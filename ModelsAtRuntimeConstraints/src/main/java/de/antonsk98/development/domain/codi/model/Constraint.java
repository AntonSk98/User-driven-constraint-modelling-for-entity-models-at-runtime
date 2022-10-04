package de.antonsk98.development.domain.codi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

/**
 * Constraint containing a focus property, type of constraint, functions applied to the focus property
 * and a message, in case a constraint is violated.
 *
 * @author Anton Skripin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Constraint {
    ConstraintType constraintType;
    String focusProperty;
    String message;
    Set<Function> functions;

    /**
     * Getter for {@link #functions}.
     * @return {@link #functions}
     */
    public Set<Function> getFunctions() {
        if (Objects.isNull(this.functions)) {
            this.functions = new HashSet<>();
        }
        return this.functions;
    }
}
