package anton.skripin.development.eumcf.configuration;


import anton.skripin.development.eumcf.mapper.ModicioInstanceMapper;
import anton.skripin.development.eumcf.mapper.ModicioModelMapper;
import anton.skripin.development.mapper.InstanceMapper;
import anton.skripin.development.mapper.ModelMapper;
import anton.skripin.development.properties.TemplateConfigurationProperties;
import anton.skripin.development.service.ConstraintDefinitionServiceImpl;
import anton.skripin.development.service.SimpleConstraintPersistenceService;
import anton.skripin.development.service.SimpleTemplateFunctionService;
import anton.skripin.development.service.api.ConstraintDefinitionService;
import anton.skripin.development.service.api.ConstraintPersistenceService;
import anton.skripin.development.service.api.TemplateFunctionService;
import modicio.core.DeepInstance;
import modicio.core.ModelElement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for constraint specification necessary implementation classes.
 */
@Configuration
public class ConstraintConfiguration {

    /**
     * Provides concrete implementation for {@link InstanceMapper<DeepInstance>} in the context of Modicio.
     *
     * @return {@link InstanceMapper<DeepInstance>}
     */
    @Bean
    public InstanceMapper<DeepInstance> instanceMapper() {
        return new ModicioInstanceMapper();
    }

    /**
     * Provides concrete implementation for {@link ModelMapper<ModelElement>} in the context of Modicio.
     *
     * @return {@link ModelMapper<ModelElement>}
     */
    @Bean
    public ModelMapper<ModelElement> modelMapper() {
        return new ModicioModelMapper();
    }


    /**
     * Provides constraint validation service to manage constraints.
     *
     * @return {@link SimpleConstraintPersistenceService}
     */
    @Bean
    public ConstraintPersistenceService constraintValidationService() {
        return new SimpleConstraintPersistenceService();
    }

    /**
     * Provides a service with all available constraint templates to be exposed to end-users.
     *
     * @param properties dynamic properties configured in {@link  TemplateConfigurationProperties}
     * @return {@link SimpleTemplateFunctionService}
     */
    @Bean
    public TemplateFunctionService templateFunctionService(TemplateConfigurationProperties properties) {
        return new SimpleTemplateFunctionService(properties);
    }

    /**
     * Provides a service to validate and check constraints.
     *
     * @param properties dynamic properties configured in {@link  TemplateConfigurationProperties}
     * @return {@link ConstraintDefinitionServiceImpl}
     */
    @Bean
    public ConstraintDefinitionService constraintDefinitionService(TemplateConfigurationProperties properties) {
        return new ConstraintDefinitionServiceImpl(properties);
    }


}
