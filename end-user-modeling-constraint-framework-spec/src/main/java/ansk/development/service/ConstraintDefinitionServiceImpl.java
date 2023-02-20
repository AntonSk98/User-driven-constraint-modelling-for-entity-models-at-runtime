package ansk.development.service;

import ansk.development.service.api.ConstraintDefinitionService;
import ansk.development.domain.template.Template;
import ansk.development.properties.TemplateConfigurationProperties;
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

    @Override
    public Template getRuntimeFunctionTemplate() {
        return templateFunctionInitializer.getRuntimeFunctionTemplate();
    }
}
