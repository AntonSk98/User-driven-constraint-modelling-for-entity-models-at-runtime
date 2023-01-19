package anton.skripin.development.repository.api;

import anton.skripin.development.domain.template.Template;

import java.util.List;

public interface TemplateFunctionRepository {
    List<Template> getAllTemplates();

    List<Template> getAllTemplatesOfFunctionType(String functionType);

    Template getTemplateByFunctionName(String functionName);

    void addNewTemplate(String functionName, String functionType, String template);

    void updateTemplate(String functionName, String functionType, String template);

    boolean deleteTemplateByFunctionName(String functionName);

    boolean deleteTemplateByFunctionNameAndType(String functionName, String functionType);
}
