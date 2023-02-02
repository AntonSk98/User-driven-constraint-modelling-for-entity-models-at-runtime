package anton.skripin.development.properties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * Class representing properties for a {@link anton.skripin.development.domain.template.Template}
 */
@Getter
@Setter
@Accessors(chain = true)
public class TemplateConfigurationProperties {

    /**
     * Placeholder value used as a template for a single-valued attribute that must be replaced by an end-user
     */
    @NonNull
    private String simplePlaceholder;

    /**
     * Placeholder value used as a template for multiple-valued attributes that must be replaced by an end-user
     */
    @NonNull
    private String arrayValuePlaceholder;

    /**
     * Placeholder value used as a template for complex or nested object entries that must be replaced by an end-user
     */
    @NonNull
    private ImmutablePair<String, String> objectPlaceholder;


    /**
     * Default constructor.
     */
    public TemplateConfigurationProperties() {
    }
}
