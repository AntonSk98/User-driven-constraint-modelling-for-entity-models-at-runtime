package ansk.development.domain.instance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * Class that defines an instance element.
 */
@Getter
@Setter
@NoArgsConstructor
public class InstanceElement {
    private String uuid;
    private String instanceOf;
    private String modelUuid;
    private List<Slot> slots;
    private List<Link> links;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstanceElement that = (InstanceElement) o;
        return Objects.equals(uuid, that.uuid) && Objects.equals(instanceOf, that.instanceOf) && Objects.equals(modelUuid, that.modelUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, instanceOf, modelUuid);
    }
}
