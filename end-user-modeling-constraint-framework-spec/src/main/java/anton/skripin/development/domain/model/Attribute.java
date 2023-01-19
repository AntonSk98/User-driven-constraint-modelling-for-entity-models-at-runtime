package anton.skripin.development.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Attribute {
    private long id;
    private String key;
    private String datatype;
    private boolean isMultiValued;
}
