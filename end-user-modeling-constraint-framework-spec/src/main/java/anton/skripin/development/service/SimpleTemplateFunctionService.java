package anton.skripin.development.service;

import anton.skripin.development.domain.template.Template;
import anton.skripin.development.exception.constraint.NoTemplateFoundException;
import anton.skripin.development.properties.TemplateConfigurationProperties;
import anton.skripin.development.service.api.TemplateFunctionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SimpleTemplateFunctionService implements TemplateFunctionService {

    private final List<Template> templateFunctions = new ArrayList<>();

    public SimpleTemplateFunctionService(TemplateConfigurationProperties templateConfigurationProperties) {
        TemplateFunctionInitializer templateFunctionInitializer = new TemplateFunctionInitializer(templateConfigurationProperties);
        templateFunctions.addAll(templateFunctionInitializer.initTemplateFunction());
    }

    @Override
    public List<Template> getAllTemplates() {
        return this.templateFunctions;
    }

    @Override
    public List<Template> getAllTemplatesOfFunctionType(String functionType) {
        return this.templateFunctions
                .stream()
                .filter(template -> template.getFunctionType().equals(functionType))
                .collect(Collectors.toList());
    }

    @Override
    public Template getTemplateByFunctionName(String functionName) {
        return this.templateFunctions
                .stream()
                .filter(template -> template.getFunctionName().equals(functionName))
                .findFirst()
                .orElseThrow(() -> new NoTemplateFoundException(functionName));
    }

    @Override
    public void addNewTemplate(String functionName, String description, String functionType, String template) {
        this.templateFunctions.add(Template.ofFunction(functionName, description, functionType, template));
    }

    @Override
    public void updateTemplate(String functionName, String description, String functionType, String templateFunction) {
        Template template = Template.ofFunction(functionName, description, functionType, templateFunction);
        Optional<Template> toBeUpdatedOptional = templateFunctions
                .stream()
                .filter(persistedTemplate ->
                        persistedTemplate.getFunctionName().equals(template.getFunctionName()) &&
                                persistedTemplate.getFunctionType().equals(template.getFunctionType()))
                .findFirst();
        if (toBeUpdatedOptional.isEmpty()) {
            this.addNewTemplate(functionName, description, functionType, templateFunction);
            return;
        }
        Template toBeUpdated = toBeUpdatedOptional.get();
        toBeUpdated.setUuid(template.getUuid());
        toBeUpdated.setTemplate(template.getTemplate());
        toBeUpdated.setFunctionName(template.getFunctionName());
        toBeUpdated.setFunctionType(template.getFunctionType());
    }

    @Override
    public boolean deleteTemplateByFunctionName(String functionName) {
        return templateFunctions.removeIf(persistedTemplate -> persistedTemplate.getFunctionName().equals(functionName));
    }

    @Override
    public boolean deleteTemplateByFunctionNameAndType(String functionName, String functionType) {
        return templateFunctions.removeIf(persistedTemplate ->
                persistedTemplate.getFunctionName().equals(functionName)
                        && persistedTemplate.getFunctionType().equals(functionType));
    }
}
