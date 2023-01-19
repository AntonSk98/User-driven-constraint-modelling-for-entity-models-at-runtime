package anton.skripin.development.domain.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ModelElement {
    private long id;
    private String name;
    private List<Attribute> attributes;
    private List<Association> associations;
}
