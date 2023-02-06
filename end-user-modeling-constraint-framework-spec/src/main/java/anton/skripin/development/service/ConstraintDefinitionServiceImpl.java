package anton.skripin.development.service;

import anton.skripin.development.domain.template.Template;
import anton.skripin.development.properties.TemplateConfigurationProperties;
import anton.skripin.development.service.api.ConstraintDefinitionService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConstraintDefinitionServiceImpl implements ConstraintDefinitionService {

    private final TemplateConfigurationProperties properties;
    private final ObjectMapper objectMapper;
    TemplateFunctionInitializer templateFunctionInitializer;

    public ConstraintDefinitionServiceImpl(TemplateConfigurationProperties properties) {
        this.templateFunctionInitializer = new TemplateFunctionInitializer(properties);
        this.objectMapper = new ObjectMapper();
        this.properties = properties;
    }

    @Override
    public Template getConstraintTemplate() {
        return getConstraintTemplate(null, null);
    }

    @Override
    public Template getConstraintTemplateWithUuid(String modelElementUuid) {
        return getConstraintTemplate(modelElementUuid, null);
    }

    @Override
    public Template getConstraintTemplateWithType(String modelElementType) {
        return getConstraintTemplate(null, modelElementType);
    }

    @Override
    public Template getConstraintTemplate(String modelElementUuid, String modelElementType) {
        return templateFunctionInitializer.getConstraintTemplate(modelElementUuid, modelElementType);
    }
}
