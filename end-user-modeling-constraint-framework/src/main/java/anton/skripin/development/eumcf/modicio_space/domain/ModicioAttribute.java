package anton.skripin.development.eumcf.modicio_space.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModicioAttribute {
    String name;
    String datatype;
    boolean nonEmpty;
}
