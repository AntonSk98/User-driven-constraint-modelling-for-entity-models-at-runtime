package anton.skripin.development.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Element that represents a relationship between two classes.
 */
@Getter
@Setter
@NoArgsConstructor
public class Association {
    private String uuid;
    private String name;
    private String path;
    private String multiplicity;
    private String targetModelElementUuid;
    private String targetModelElementName;
}
