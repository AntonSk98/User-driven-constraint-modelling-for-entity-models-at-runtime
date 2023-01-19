package anton.skripin.development.repository;

import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import anton.skripin.development.domain.constraint.functions.FunctionType;
import anton.skripin.development.domain.constraint.functions.LogicalFunction;
import anton.skripin.development.domain.constraint.functions.StringBasedFunction;
import anton.skripin.development.domain.template.Template;
import anton.skripin.development.exception.ConstraintTemplateCreationException;
import anton.skripin.development.exception.NoTemplateFoundException;
import anton.skripin.development.properties.TemplateConfigurationProperties;
import anton.skripin.development.registry.TemplateRegistry;
import anton.skripin.development.repository.api.TemplateFunctionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class TemplateFunctionRepositoryImpl implements TemplateFunctionRepository {

    private final ObjectMapper objectMapper;
    private final TemplateConfigurationProperties properties;
    private final TemplateRegistry templateRegistry;
    Logger logger = LoggerFactory.getLogger(TemplateFunctionRepositoryImpl.class);

    public TemplateFunctionRepositoryImpl(TemplateConfigurationProperties properties) {
        this.properties = properties;
        this.objectMapper = new ObjectMapper();
        this.templateRegistry = new TemplateRegistry();
        initTemplates();
    }

    @Override
    public List<Template> getAllTemplates() {
        return new ArrayList<>(this.templateRegistry.getTemplates());
    }

    @Override
    public List<Template> getAllTemplatesOfFunctionType(String functionType) {
        return this.templateRegistry
                .getTemplates()
                .stream()
                .filter(template -> template.getFunctionType().equals(functionType))
                .collect(Collectors.toList());
    }

    @Override
    public Template getTemplateByFunctionName(String functionName) {
        return this.templateRegistry
                .getTemplates()
                .stream()
                .filter(template -> template.getFunctionName().equals(functionName))
                .findFirst()
                .orElseThrow(() -> new NoTemplateFoundException(functionName));
    }

    @Override
    public void addNewTemplate(String functionName, String functionType, String template) {
        this.templateRegistry.createTemplate(Template.of(functionName, functionType, template));
    }

    @Override
    public void updateTemplate(String functionName, String functionType, String template) {
        this.templateRegistry.updateTemplate(Template.of(functionName, functionType, template));
    }

    @Override
    public boolean deleteTemplateByFunctionName(String functionName) {
        return this.templateRegistry.deleteTemplateByFunctionName(functionName);
    }

    @Override
    public boolean deleteTemplateByFunctionNameAndType(String functionName, String functionType) {
        return this.templateRegistry.deleteTemplateByFunctionNameAndType(functionName, functionType);
    }

    private void initTemplates() {
        createLogicalFunctionTemplate("AND");
        createLogicalFunctionTemplate("OR");
        createStringBasedFunctionTemplate("MIN_LENGTH", "min_length");
        createStringBasedFunctionTemplate("MAX_LENGTH", "max_length");
    }

    private void createLogicalFunctionTemplate(String name) {
        ConstraintFunction logicalFunction = new LogicalFunction(name, Collections.emptyList());
        try {
            String template = objectMapper
                    .writeValueAsString(logicalFunction)
                    .replace("[]", String.format("[%s]", properties.getPlaceholder()));
            logger.debug("Adding template: {}", template);
            templateRegistry.addTemplate(Template.of(name, FunctionType.LOGICAL_FUNCTION, template));
        } catch (JsonProcessingException e) {
            throw new ConstraintTemplateCreationException(name);
        }
    }

    private void createStringBasedFunctionTemplate(String name, String... params) {
        String placeholder = properties.getPlaceholder();
        Map<String, String> templateParams = new HashMap<>();
        Arrays.stream(params).forEach(param -> templateParams.put(param, placeholder));
        ConstraintFunction constraintFunction = new StringBasedFunction(name, placeholder, placeholder, templateParams);
        try {
            String template = objectMapper.writeValueAsString(constraintFunction);
            templateRegistry.addTemplate(Template.of(name, FunctionType.STRING_BASED_FUNCTION, template));
        } catch (JsonProcessingException e) {
            throw new ConstraintTemplateCreationException(name);
        }
    }
}
