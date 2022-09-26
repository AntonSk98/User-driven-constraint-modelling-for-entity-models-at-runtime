package de.antonsk98.development.domain.codi.deepInstance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Container for clabject metadata.
 *
 * @author Anton Skripin
 */
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class ClabjectData {
    private String instanceId;
    private String instanceOf;
    private String identity;
}
