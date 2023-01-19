package anton.skripin.development.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateConfigurationProperties {
    private final String placeholder;

    public TemplateConfigurationProperties(String placeholder) {
        this.placeholder = placeholder;
    }
}
