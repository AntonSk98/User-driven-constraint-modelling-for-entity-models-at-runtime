package anton.skripin.development.domain.instance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Link {
    private String instanceUuid;
    private String name;
    private String targetInstanceUuid;
}
