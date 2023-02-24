/*
 * Copyright (c) 2023 Anton Skripin
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package ansk.development.service;

import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.ViolationLevel;
import ansk.development.domain.constraint.functions.ConstraintFunction;
import ansk.development.domain.constraint.functions.FunctionDescription;
import ansk.development.domain.constraint.functions.FunctionMetadata;
import ansk.development.domain.constraint.functions.FunctionType;
import ansk.development.domain.constraint.functions.types.*;
import ansk.development.domain.template.ObjectTemplatePlaceholder;
import ansk.development.domain.template.Template;
import ansk.development.exception.constraint.ConstraintTemplateCreationException;
import ansk.development.properties.TemplateConfigurationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class TemplateFunctionInitializer {

    private final TemplateConfigurationProperties templateProps;

    public TemplateFunctionInitializer(TemplateConfigurationProperties templateProps) {
        this.templateProps = templateProps;
    }

    public List<Template> initTemplateFunction() {
        return List.of(
                createLogicalFunctionTemplate(FunctionMetadata.FunctionNames.AND),
                createLogicalFunctionTemplate(FunctionMetadata.FunctionNames.OR),

                createConditionBasedTemplate(FunctionMetadata.FunctionNames.IF_THEN, false),
                createConditionBasedTemplate(FunctionMetadata.FunctionNames.IF_THEN_ELSE, true),

                createStringBasedFunctionTemplate(FunctionMetadata.FunctionNames.MIN_LENGTH, FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES.get(FunctionMetadata.FunctionNames.MIN_LENGTH)),
                createStringBasedFunctionTemplate(FunctionMetadata.FunctionNames.MAX_LENGTH, FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES.get(FunctionMetadata.FunctionNames.MAX_LENGTH)),
                createStringBasedFunctionTemplate(FunctionMetadata.FunctionNames.UNIQUE, Collections.emptyList()),
                createStringBasedFunctionTemplate(FunctionMetadata.FunctionNames.NOT_NULL_OR_EMPTY, Collections.emptyList()),

                createCollectionBasedFunction(FunctionMetadata.FunctionNames.FOR_ALL, Collections.emptyList()),
                createCollectionBasedFunction(FunctionMetadata.FunctionNames.FOR_SOME, Collections.emptyList()),
                createCollectionBasedFunction(FunctionMetadata.FunctionNames.FOR_NONE, Collections.emptyList()),
                createCollectionBasedFunction(FunctionMetadata.FunctionNames.FOR_EXACTLY, FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES.get(FunctionMetadata.FunctionNames.FOR_EXACTLY)),

                createRangeBasedFunctionTemplate(FunctionMetadata.FunctionNames.GREATER_THAN, FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES.get(FunctionMetadata.FunctionNames.GREATER_THAN)),
                createRangeBasedFunctionTemplate(FunctionMetadata.FunctionNames.GREATER_THAN_OR_EQUALS, FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES.get(FunctionMetadata.FunctionNames.GREATER_THAN_OR_EQUALS)),
                createRangeBasedFunctionTemplate(FunctionMetadata.FunctionNames.LESS_THAN, FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES.get(FunctionMetadata.FunctionNames.LESS_THAN)),
                createRangeBasedFunctionTemplate(FunctionMetadata.FunctionNames.LESS_THAN_OR_EQUALS, FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES.get(FunctionMetadata.FunctionNames.LESS_THAN_OR_EQUALS)),
                createRangeBasedFunctionTemplate(FunctionMetadata.FunctionNames.EQUALS, FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES.get(FunctionMetadata.FunctionNames.EQUALS)),

                createAssociationBasedTemplate(FunctionMetadata.FunctionNames.MIN_CARDINALITY, FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES.get(FunctionMetadata.FunctionNames.MIN_CARDINALITY)),
                createAssociationBasedTemplate(FunctionMetadata.FunctionNames.MAX_CARDINALITY, FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES.get(FunctionMetadata.FunctionNames.MAX_CARDINALITY))
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
            return Template.ofFunction(name, FunctionDescription.descriptionByName(name), FunctionType.COLLECTION_BASED_FUNCTION, template);
        } catch (JsonProcessingException e) {
            throw new ConstraintTemplateCreationException(name);
        }
    }

    private Template createConditionBasedTemplate(String name, boolean withElseClause) {
        var objectPlaceholder = templateProps.objectKeyValuePlaceholder();
        List<ConstraintFunction> clauses = withElseClause ? List.of(
                new ObjectTemplatePlaceholder("Condition", objectPlaceholder.getRight()),
                new ObjectTemplatePlaceholder("Then clause", objectPlaceholder.getRight()),
                new ObjectTemplatePlaceholder("Else clause", objectPlaceholder.getRight())
        ) : List.of(
                new ObjectTemplatePlaceholder("Condition", objectPlaceholder.getRight()),
                new ObjectTemplatePlaceholder("Then clause", objectPlaceholder.getRight())
        );
        try {
            String template = new ObjectMapper().writeValueAsString(new ConditionalBasedFunction(name, clauses));
            return Template.ofFunction(name, FunctionDescription.descriptionByName(name), FunctionType.CONDITIONAL_BASED_FUNCTION, template);
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
            return Template.ofFunction(name, FunctionDescription.descriptionByName(name), FunctionType.LOGICAL_FUNCTION, template);
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
            return Template.ofFunction(name, FunctionDescription.descriptionByName(name), FunctionType.STRING_BASED_FUNCTION, template);
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
            return Template.ofFunction(name, FunctionDescription.descriptionByName(name), FunctionType.RANGE_BASED_FUNCTION, template);
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
            return Template.ofFunction(name, FunctionDescription.descriptionByName(name), FunctionType.ASSOCIATION_BASED_FUNCTION, template);
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

    public Template getRuntimeFunctionTemplate() {
        RuntimeFunction runtimeFunction = new RuntimeFunction(templateProps.constraintNamePlaceholder(), templateProps.runtimeFunctionPlaceholder());
        try {
            var functionTemplate = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(runtimeFunction);
            return Template.ofFunction(runtimeFunction.getName(), "", FunctionType.RUNTIME_FUNCTION, functionTemplate);
        } catch (JsonProcessingException e) {
            throw new ConstraintTemplateCreationException();
        }
    }
}
