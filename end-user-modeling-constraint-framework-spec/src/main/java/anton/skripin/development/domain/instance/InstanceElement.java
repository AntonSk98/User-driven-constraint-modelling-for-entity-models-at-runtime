package anton.skripin.development.domain.instance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InstanceElement {
    private String uuid;
    private String instanceOf;
    private String modelUuid;
    private List<Slot> slots;
    private List<Link> links;
}
