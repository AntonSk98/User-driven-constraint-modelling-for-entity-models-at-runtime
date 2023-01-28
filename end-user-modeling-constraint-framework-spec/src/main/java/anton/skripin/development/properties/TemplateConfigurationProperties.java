package anton.skripin.development.properties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class TemplateConfigurationProperties {

    @NonNull
    private String simplePlaceholder;

    @NonNull
    private String arrayValuePlaceholder;

    @NonNull
    private ImmutablePair<String, String> objectPlaceholder;

    public TemplateConfigurationProperties() {
    }
}
