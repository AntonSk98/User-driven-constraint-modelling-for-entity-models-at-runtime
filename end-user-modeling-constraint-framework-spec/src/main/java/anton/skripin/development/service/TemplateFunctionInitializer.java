package anton.skripin.development.service;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.ViolationLevel;
import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import anton.skripin.development.domain.constraint.functions.FunctionType;
import anton.skripin.development.domain.constraint.functions.types.CollectionBasedFunction;
import anton.skripin.development.domain.constraint.functions.types.LogicalFunction;
import anton.skripin.development.domain.constraint.functions.types.StringBasedFunction;
import anton.skripin.development.domain.template.ObjectTemplatePlaceholder;
import anton.skripin.development.domain.template.Template;
import anton.skripin.development.exception.constraint.ConstraintTemplateCreationException;
import anton.skripin.development.properties.TemplateConfigurationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import static anton.skripin.development.domain.constraint.functions.FunctionDescription.*;

public class TemplateFunctionInitializer {

    private final TemplateConfigurationProperties templateProps;

    public TemplateFunctionInitializer(TemplateConfigurationProperties templateProps) {
        this.templateProps = templateProps;
    }

    public List<Template> initTemplateFunction() {
        return List.of(
                createLogicalFunctionTemplate(AND),
                createLogicalFunctionTemplate(OR),
                createStringBasedFunctionTemplate(MIN_LENGTH, "min_length"),
                createStringBasedFunctionTemplate(MAX_LENGTH, "max_length"),
                createCollectionBasedFunction(FOR_ALL)
        );
    }

    private Template createCollectionBasedFunction(String name) {
        var objectTemplate = templateProps.objectKeyValuePlaceholder();
        ConstraintFunction collectionBasedFunction = new CollectionBasedFunction(name,
                templateProps.navigationPlaceholder(),
                new ObjectTemplatePlaceholder(objectTemplate.getLeft(), objectTemplate.getRight()), true);
        try {
            String template = new ObjectMapper()
                    .writeValueAsString(collectionBasedFunction);
            return Template.ofFunction(name, descriptionByName(name), FunctionType.COLLECTION_BASED_FUNCTION, template);
        } catch (JsonProcessingException e) {
            throw new ConstraintTemplateCreationException(name);
        }
    }

    private Template createLogicalFunctionTemplate(String name) {
        var objectPlaceholder = templateProps.objectKeyValuePlaceholder();
        ConstraintFunction logicalFunction = new LogicalFunction(name,
                List.of(
                        new ObjectTemplatePlaceholder(objectPlaceholder.getLeft(), objectPlaceholder.getRight()),
                        new ObjectTemplatePlaceholder(objectPlaceholder.getLeft(), objectPlaceholder.getRight())
                ));
        try {
            String template = new ObjectMapper().writeValueAsString(logicalFunction);
            return Template.ofFunction(name, descriptionByName(name), FunctionType.LOGICAL_FUNCTION, template);
        } catch (JsonProcessingException e) {
            throw new ConstraintTemplateCreationException(name);
        }
    }

    private Template createStringBasedFunctionTemplate(String name, String... params) {
        Map<String, String> templateParams = new HashMap<>();
        Arrays.stream(params).forEach(param -> templateParams.put(param, templateProps.paramValuePlaceholder()));
        ConstraintFunction constraintFunction = new StringBasedFunction(name,
                templateProps.attributePlaceholder(),
                templateParams,
                true);
        try {
            String template = new ObjectMapper().writeValueAsString(constraintFunction);
            return Template.ofFunction(name, descriptionByName(name), FunctionType.STRING_BASED_FUNCTION, template);
        } catch (JsonProcessingException e) {
            throw new ConstraintTemplateCreationException(name);
        }
    }

    public Template getConstraintTemplate(String modelElementUuid, String modelElementType) {
        Constraint constraint = new Constraint();
        constraint.setUuid(UUID.randomUUID().toString());
        constraint.setName(templateProps.constraintNamePlaceholder());
        constraint.setViolationLevel(ViolationLevel.ERROR);
        constraint.setViolationMessage(templateProps.violationMessagePlaceholder());
        if (modelElementUuid != null) {
            constraint.setModelElementUuid(modelElementUuid);
        }
        if (modelElementType != null) {
            constraint.setModelElementType(modelElementType);
        }
        var objectTemplate = templateProps.objectKeyValuePlaceholder();
        constraint.setConstraintFunction(new ObjectTemplatePlaceholder(objectTemplate.getLeft(), objectTemplate.getRight()));
        try {
            String constraintTemplate = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(constraint);
            return Template.ofConstraint(constraintTemplate);
        } catch (JsonProcessingException e) {
            throw new ConstraintTemplateCreationException();
        }
    }
}
