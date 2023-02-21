package ansk.development.eumcf.configuration;


import ansk.development.eumcf.mapper.ModicioInstanceMapper;
import ansk.development.eumcf.mapper.ModicioModelMapper;
import ansk.development.mapper.InstanceMapper;
import ansk.development.mapper.ModelMapper;
import ansk.development.properties.TemplateConfigurationProperties;
import ansk.development.service.AbstractConstraintValidationService;
import ansk.development.service.ConstraintDefinitionServiceImpl;
import ansk.development.service.SimpleConstraintPersistenceService;
import ansk.development.service.SimpleTemplateFunctionService;
import ansk.development.service.api.ConstraintDefinitionService;
import ansk.development.service.api.ConstraintPersistenceService;
import ansk.development.service.api.ConstraintValidationService;
import ansk.development.service.api.TemplateFunctionService;
import modicio.core.DeepInstance;
import modicio.core.ModelElement;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    public ConstraintPersistenceService constraintPersistenceService() {
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
     * Provides a service to define constraints.
     *
     * @param properties dynamic properties configured in {@link  TemplateConfigurationProperties}
     * @return {@link ConstraintDefinitionServiceImpl}
     */
    @Bean
    public ConstraintDefinitionService constraintDefinitionService(TemplateConfigurationProperties properties) {
        return new ConstraintDefinitionServiceImpl(properties);
    }

}
