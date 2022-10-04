package de.antonsk98.development.domain.codi.deepInstance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain of an association of a {@link Clabject}.
 *
 * @author Anton Skripin.
 */
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class AssociationData {
    private Long id;
    private String byRelation;
    private String instanceId;
    private String targetInstanceId;
    private Boolean isFinal;
}