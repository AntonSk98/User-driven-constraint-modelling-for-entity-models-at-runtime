package anton.skripin.development.service;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.ViolationLevel;
import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import anton.skripin.development.domain.constraint.functions.FunctionType;
import anton.skripin.development.domain.constraint.functions.types.*;
import anton.skripin.development.domain.template.ObjectTemplatePlaceholder;
import anton.skripin.development.domain.template.Template;
import anton.skripin.development.exception.constraint.ConstraintTemplateCreationException;
import anton.skripin.development.properties.TemplateConfigurationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import static anton.skripin.development.domain.constraint.functions.FunctionDescription.descriptionByName;
import static anton.skripin.development.domain.constraint.functions.FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES;
import static anton.skripin.development.domain.constraint.functions.FunctionMetadata.FunctionNames.*;

public class TemplateFunctionInitializer {

    private final TemplateConfigurationProperties templateProps;

    public TemplateFunctionInitializer(TemplateConfigurationProperties templateProps) {
        this.templateProps = templateProps;
    }

    public List<Template> initTemplateFunction() {
        return List.of(
                createLogicalFunctionTemplate(AND),
                createLogicalFunctionTemplate(OR),

                createStringBasedFunctionTemplate(MIN_LENGTH, FUNCTION_TO_PARAMETER_NAMES.get(MIN_LENGTH)),
                createStringBasedFunctionTemplate(MAX_LENGTH, FUNCTION_TO_PARAMETER_NAMES.get(MAX_LENGTH)),
                createStringBasedFunctionTemplate(UNIQUE, Collections.emptyList()),
                createStringBasedFunctionTemplate(NOT_NULL_OR_EMPTY, Collections.emptyList()),

                createCollectionBasedFunction(FOR_ALL, Collections.emptyList()),
                createCollectionBasedFunction(FOR_SOME, Collections.emptyList()),
                createCollectionBasedFunction(FOR_NONE, Collections.emptyList()),
                createCollectionBasedFunction(FOR_EXACTLY, FUNCTION_TO_PARAMETER_NAMES.get(FOR_EXACTLY)),

                createRangeBasedFunctionTemplate(GREATER_THAN, FUNCTION_TO_PARAMETER_NAMES.get(GREATER_THAN)),
                createRangeBasedFunctionTemplate(GREATER_THAN_OR_EQUALS, FUNCTION_TO_PARAMETER_NAMES.get(GREATER_THAN_OR_EQUALS)),
                createRangeBasedFunctionTemplate(LESS_THAN, FUNCTION_TO_PARAMETER_NAMES.get(LESS_THAN)),
                createRangeBasedFunctionTemplate(LESS_THAN_OR_EQUALS, FUNCTION_TO_PARAMETER_NAMES.get(LESS_THAN_OR_EQUALS)),
                createRangeBasedFunctionTemplate(EQUALS, FUNCTION_TO_PARAMETER_NAMES.get(EQUALS)),

                createAssociationBasedTemplate(MIN_CARDINALITY, FUNCTION_TO_PARAMETER_NAMES.get(MIN_CARDINALITY)),
                createAssociationBasedTemplate(MAX_CARDINALITY, FUNCTION_TO_PARAMETER_NAMES.get(MAX_CARDINALITY))
        );
    }

    private Template createCollectionBasedFunction(String name, List<String> params) {
        Map<String, String> templateParams = new HashMap<>();
        params.forEach(param -> templateParams.put(param, templateProps.paramValuePlaceholder()));
        var objectTemplate = templateProps.objectKeyValuePlaceholder();
        ConstraintFunction collectionBasedFunction = new CollectionBasedFunction(name,
                templateProps.navigationPlaceholder(),
                new ObjectTemplatePlaceholder(objectTemplate.getLeft(), objectTemplate.getRight()), params.size() == 0 ? null : templateParams, true);
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

    private Template createStringBasedFunctionTemplate(String name, List<String> params) {
        Map<String, String> templateParams = new HashMap<>();
        params.forEach(param -> templateParams.put(param, templateProps.paramValuePlaceholder()));
        ConstraintFunction constraintFunction = new StringBasedFunction(name,
                templateProps.attributePlaceholder(),
                params.size() == 0 ? null : templateParams,
                true);
        try {
            String template = new ObjectMapper().writeValueAsString(constraintFunction);
            return Template.ofFunction(name, descriptionByName(name), FunctionType.STRING_BASED_FUNCTION, template);
        } catch (JsonProcessingException e) {
            throw new ConstraintTemplateCreationException(name);
        }
    }

    private Template createRangeBasedFunctionTemplate(String name, List<String> params) {
        Map<String, String> templateParams = new HashMap<>();
        params.forEach(param -> templateParams.put(param, templateProps.paramValuePlaceholder()));
        ConstraintFunction constraintFunction = new RangeBasedFunction(name,
                templateProps.attributePlaceholder(),
                templateParams,
                true);
        try {
            String template = new ObjectMapper().writeValueAsString(constraintFunction);
            return Template.ofFunction(name, descriptionByName(name), FunctionType.RANGE_BASED_FUNCTION, template);
        } catch (JsonProcessingException e) {
            throw new ConstraintTemplateCreationException(name);
        }
    }

    private Template createAssociationBasedTemplate(String name, List<String> params) {
        Map<String, String> templateParams = new HashMap<>();
        params.forEach(param -> templateParams.put(param, templateProps.paramValuePlaceholder()));
        ConstraintFunction constraintFunction = new AssociationBasedFunction(name, templateParams);
        try {
            String template = new ObjectMapper().writeValueAsString(constraintFunction);
            return Template.ofFunction(name, descriptionByName(name), FunctionType.ASSOCIATION_BASED_FUNCTION, template);
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
