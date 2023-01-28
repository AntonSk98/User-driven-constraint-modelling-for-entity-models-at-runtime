package anton.skripin.development.service;

import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import anton.skripin.development.domain.constraint.functions.FunctionType;
import anton.skripin.development.domain.constraint.functions.types.LogicalFunction;
import anton.skripin.development.domain.constraint.functions.types.StringBasedFunction;
import anton.skripin.development.domain.template.Template;
import anton.skripin.development.exception.ConstraintTemplateCreationException;
import anton.skripin.development.properties.TemplateConfigurationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class TemplateFunctionInitializer {

    private final TemplateConfigurationProperties templateConfigurationProperties;

    public TemplateFunctionInitializer(TemplateConfigurationProperties templateConfigurationProperties) {
        this.templateConfigurationProperties = templateConfigurationProperties;
    }

    List<Template> initTemplateFunction() {
        return List.of(
                createLogicalFunctionTemplate("AND"),
                createLogicalFunctionTemplate("OR"),
                createStringBasedFunctionTemplate("MIN_LENGTH", "min_length"),
                createStringBasedFunctionTemplate("MAX_LENGTH", "max_length")

        );
    }

    private Template createLogicalFunctionTemplate(String name) {
        ConstraintFunction logicalFunction = new LogicalFunction(name, Collections.emptyList());
        try {
            String template = new ObjectMapper()
                    .writeValueAsString(logicalFunction)
                    .replace("[]", String.format("[\"%s\"]", templateConfigurationProperties.getArrayValuePlaceholder()));
            return Template.ofFunction(name, FunctionType.LOGICAL_FUNCTION, template);
        } catch (JsonProcessingException e) {
            throw new ConstraintTemplateCreationException(name);
        }
    }

    private Template createStringBasedFunctionTemplate(String name, String... params) {
        String placeholder = templateConfigurationProperties.getSimplePlaceholder();
        Map<String, String> templateParams = new HashMap<>();
        Arrays.stream(params).forEach(param -> templateParams.put(param, placeholder));
        ConstraintFunction constraintFunction = new StringBasedFunction(name, placeholder, templateParams);
        try {
            String template = new ObjectMapper().writeValueAsString(constraintFunction);
            return Template.ofFunction(name, FunctionType.STRING_BASED_FUNCTION, template);
        } catch (JsonProcessingException e) {
            throw new ConstraintTemplateCreationException(name);
        }
    }
}
