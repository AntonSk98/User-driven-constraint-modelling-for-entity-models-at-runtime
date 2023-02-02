package anton.skripin.development.domain.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Represents an object or a set of objects that share a common structure and behavior
 */
@Getter
@Setter
@NoArgsConstructor
public class ModelElement {
    private String uuid;
    private String name;
    private List<Attribute> attributes;
    private List<Association> associations;
}
