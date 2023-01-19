package anton.skripin.development.eumcf.configuration;


import anton.skripin.development.mapper.AbstractToPSConstraintMapper;
import anton.skripin.development.mapper.ModelMapper;
import anton.skripin.development.mapper.SimpleAbstractToPSConstraintMapper;
import anton.skripin.development.mapper.SimpleModelMapper;
import anton.skripin.development.properties.TemplateConfigurationProperties;
import anton.skripin.development.service.ConstraintDefinitionServiceImpl;
import anton.skripin.development.service.ModelMapperServiceImpl;
import anton.skripin.development.service.TemplateFunctionServiceImpl;
import anton.skripin.development.service.api.ConstraintDefinitionService;
import anton.skripin.development.service.api.ModelMapperService;
import anton.skripin.development.service.api.TemplateFunctionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfiguration {

    @Bean
    public TemplateFunctionService templateFunctionService(TemplateConfigurationProperties properties) {
        return new TemplateFunctionServiceImpl(properties);
    }

    @Bean
    public ModelMapperService<?> modelMapperService() {
        ModelMapper<?> modelMapper = new SimpleModelMapper();
        return new ModelMapperServiceImpl<>(modelMapper);
    }

    @Bean
    public ConstraintDefinitionService constraintDefinitionService(TemplateConfigurationProperties properties) {
        AbstractToPSConstraintMapper constraintMapper = new SimpleAbstractToPSConstraintMapper();
        return new ConstraintDefinitionServiceImpl(properties, constraintMapper);
    }


}
