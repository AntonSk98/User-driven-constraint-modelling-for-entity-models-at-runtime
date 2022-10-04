package de.antonsk98.development.domain.codi.deepInstance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain of extension data for {@link Clabject}.
 *
 * @author Anton Skripin
 */
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class ExtensionData {
    private Long id;
    private String instanceId;
    private String parentInstanceId;
}
