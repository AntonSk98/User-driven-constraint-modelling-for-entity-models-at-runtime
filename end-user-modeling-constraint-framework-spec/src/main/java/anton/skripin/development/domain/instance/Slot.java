package anton.skripin.development.domain.instance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain element of an instantiated attribute.
 */
@Getter
@Setter
@NoArgsConstructor
public class Slot {
    private String instanceUuid;
    private String key;
    private String value;
}
