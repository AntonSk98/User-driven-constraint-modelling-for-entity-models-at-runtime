package de.antonsk98.development.domain.codi.deepInstance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Clabject representations of a codi model.
 *
 * @author Anton Skripin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Clabject {
    private ClabjectData clabjectData;
    private Set<ExtensionData> extensionData;
    private Set<AttributeData> attributeData;
    private Set<AssociationData> associationData;
}
