package de.antonsk98.development.domain.codi.deepInstance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain of an attribute of a {@link Clabject}.
 *
 * @author Anton Skripin
 */
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class AttributeData {
    private Long id;
    private String instanceId;
    private String key;
    private String value;
    private Boolean isFinal;
}
