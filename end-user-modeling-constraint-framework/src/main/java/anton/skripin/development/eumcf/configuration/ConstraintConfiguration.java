package anton.skripin.development.eumcf.configuration;


import anton.skripin.development.eumcf.modicio_space.mapper.ModicioInstanceMapper;
import anton.skripin.development.eumcf.modicio_space.mapper.ModicioModelMapper;
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

@Configuration
public class ConstraintConfiguration {

    @Bean
    public InstanceMapper<DeepInstance> instanceMapper() {
        return new ModicioInstanceMapper();
    }

    @Bean
    public ModelMapper<ModelElement> modelMapper() {
        return new ModicioModelMapper();
    }


    @Bean
    public ConstraintPersistenceService constraintValidationService() {
        return new SimpleConstraintPersistenceService();
    }

    @Bean
    public TemplateFunctionService templateFunctionService(TemplateConfigurationProperties properties) {
        return new SimpleTemplateFunctionService(properties);
    }

    @Bean
    public ConstraintDefinitionService constraintDefinitionService(TemplateConfigurationProperties properties) {
        return new ConstraintDefinitionServiceImpl(properties);
    }


}
