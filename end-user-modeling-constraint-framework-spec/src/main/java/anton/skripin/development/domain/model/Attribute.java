package anton.skripin.development.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Represents a data definition for an instance of a classifier
 */
@Getter
@Setter
@NoArgsConstructor
public class Attribute {
    private String uuid;
    private String key;
    private String datatype;
    private String path;
    private boolean isMultiValued;
}
