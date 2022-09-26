package de.antonsk98.development.domain.codi.deepInstance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Container collecting all clabjects of a deep-instance hierarchy.
 *
 * @author Anton Skripin
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeepInstance {
    private Set<Clabject> clabjects;
}
