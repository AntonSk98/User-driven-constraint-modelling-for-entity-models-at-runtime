package anton.skripin.development.eumcf.configuration;

import anton.skripin.development.properties.DefaultTemplateConfigurationProperties;
import anton.skripin.development.properties.TemplateConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration file for dynamic properties of a constraint specification.
 */
@Configuration
public class ConfigurationProperties {

    /**
     * Bean for {@link TemplateConfigurationProperties}.
     *
     * @return {@link TemplateConfigurationProperties}
     */
    @Bean
    public TemplateConfigurationProperties templateConfigurationProperties() {
        return new DefaultTemplateConfigurationProperties();
    }
}
