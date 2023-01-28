package anton.skripin.development.eumcf.simulation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Registry {
    private ModelRegistry modelRegistry;
    private InstanceRegistry instanceRegistry;
}
