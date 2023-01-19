package anton.skripin.development.eumcf.configuration;

import anton.skripin.development.properties.GatewayConfigurationProperties;
import anton.skripin.development.properties.TemplateConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationProperties {
    @Bean
    public GatewayConfigurationProperties gatewayConfigurationProperties() {
        return new GatewayConfigurationProperties("localhost:8100/get_model_element_by_id", "id");
    }

    @Bean
    public TemplateConfigurationProperties templateConfigurationProperties() {
        return new TemplateConfigurationProperties("???");
    }
}
