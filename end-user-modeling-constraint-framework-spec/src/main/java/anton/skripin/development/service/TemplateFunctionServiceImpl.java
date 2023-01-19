package anton.skripin.development.service;

import anton.skripin.development.domain.template.Template;
import anton.skripin.development.properties.TemplateConfigurationProperties;
import anton.skripin.development.repository.TemplateFunctionRepositoryImpl;
import anton.skripin.development.repository.api.TemplateFunctionRepository;
import anton.skripin.development.service.api.TemplateFunctionService;

import java.util.List;

public class TemplateFunctionServiceImpl implements TemplateFunctionService {

    private final TemplateFunctionRepository templateFunctionRepository;

    public TemplateFunctionServiceImpl(TemplateConfigurationProperties templateConfigurationProperties) {
        this.templateFunctionRepository = new TemplateFunctionRepositoryImpl(templateConfigurationProperties);
    }

    @Override
    public List<Template> getAllTemplates() {
        return this.templateFunctionRepository.getAllTemplates();
    }

    @Override
    public List<Template> getAllTemplatesOfFunctionType(String functionType) {
        return this.templateFunctionRepository.getAllTemplatesOfFunctionType(functionType);
    }

    @Override
    public Template getTemplateByFunctionName(String functionName) {
        return this.templateFunctionRepository.getTemplateByFunctionName(functionName);
    }

    @Override
    public void addNewTemplate(String functionName, String functionType, String template) {
        this.templateFunctionRepository.addNewTemplate(functionName, functionType, template);
    }

    @Override
    public void updateTemplate(String functionName, String functionType, String template) {
        this.templateFunctionRepository.updateTemplate(functionName, functionType, template);
    }

    @Override
    public boolean deleteTemplateByFunctionName(String functionName) {
        return this.templateFunctionRepository.deleteTemplateByFunctionName(functionName);
    }

    @Override
    public boolean deleteTemplateByFunctionNameAndType(String functionName, String functionType) {
        return this.templateFunctionRepository.deleteTemplateByFunctionNameAndType(functionName, functionType);
    }
}
