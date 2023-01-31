package anton.skripin.development.domain.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BackwardLinks {

    private final List<String> backwardLinkConstraints = new ArrayList<>();

    public void addBackwardLinkConstraint(String constraintUuid) {
        this.backwardLinkConstraints.add(constraintUuid);
    }

    public void removeBackwardLinkConstraint(String constraintUuid) {
        this.backwardLinkConstraints.remove(constraintUuid);
    }
}
