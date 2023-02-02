package anton.skripin.development.domain.instance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain element of an instantiated association.
 * It consists of a name, source, and target uuid
 */
@Getter
@Setter
@NoArgsConstructor
public class Link {
    private String instanceUuid;
    private String name;
    private String targetInstanceUuid;
}
