package anton.skripin.development.registry;

import anton.skripin.development.domain.template.Template;
import anton.skripin.development.exception.NotInitializedRegistryException;
import anton.skripin.development.exception.UniqueFunctionNameExcpetion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TemplateRegistry {

    private final List<Template> templates;

    public TemplateRegistry() {
        this.templates = new ArrayList<>();
    }

    public void addTemplate(Template template) {
        this.templates.add(template);
    }

    public void updateTemplate(Template template) {
        Optional<Template> toBeUpdatedOptional = templates
                .stream()
                .filter(persistedTemplate ->
                        persistedTemplate.getFunctionName().equals(template.getFunctionName()) &&
                                persistedTemplate.getFunctionType().equals(template.getFunctionType()))
                .findFirst();
        if (toBeUpdatedOptional.isEmpty()) {
            this.createTemplate(template);
            return;
        }
        Template toBeUpdated = toBeUpdatedOptional.get();
        toBeUpdated.setUuid(template.getUuid());
        toBeUpdated.setTemplate(template.getTemplate());
        toBeUpdated.setFunctionName(template.getFunctionName());
        toBeUpdated.setFunctionType(template.getFunctionType());
    }

    public void createTemplate(Template template) {
        checkUniqueFunctionName(template.getFunctionName());
        templates.add(template);
    }

    public boolean deleteTemplateByFunctionName(String functionName) {
        return templates.removeIf(persistedTemplate -> persistedTemplate.getFunctionName().equals(functionName));
    }

    public boolean deleteTemplateByFunctionNameAndType(String functionName, String functionType) {
        return templates.removeIf(persistedTemplate ->
                persistedTemplate.getFunctionName().equals(functionName)
                        && persistedTemplate.getFunctionType().equals(functionType));
    }

    public boolean deleteTemplate(Template template) {
        return templates.remove(template);
    }

    public List<Template> getTemplates() {
        return templates;
    }

    private void checkUniqueFunctionName(String functionName) {
        if (this.templates.stream().anyMatch(template -> template.getFunctionName().equals(functionName))) {
            throw new UniqueFunctionNameExcpetion(functionName);
        }
    }
}
