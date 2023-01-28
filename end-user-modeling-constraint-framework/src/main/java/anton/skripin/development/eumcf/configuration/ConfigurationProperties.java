package anton.skripin.development.eumcf.configuration;

import anton.skripin.development.properties.TemplateConfigurationProperties;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationProperties {

    @Bean
    public TemplateConfigurationProperties templateConfigurationProperties() {
        return new TemplateConfigurationProperties()
                .setSimplePlaceholder("Replace with concrete value!")
                .setArrayValuePlaceholder("Add multiple functions here separated by comma!")
                .setObjectPlaceholder(new ImmutablePair<>("Hint", "Remove this comment object entirely and click on one of the function!"));
    }
}