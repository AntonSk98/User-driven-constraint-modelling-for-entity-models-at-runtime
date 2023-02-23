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

import ansk.development.domain.template.Template;
import ansk.development.exception.constraint.NoTemplateFoundException;
import ansk.development.properties.TemplateConfigurationProperties;
import ansk.development.service.api.TemplateFunctionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SimpleTemplateFunctionService implements TemplateFunctionService {

    private final List<Template> templateFunctions = new ArrayList<>();
    private final TemplateFunctionInitializer templateFunctionInitializer;

    public SimpleTemplateFunctionService(TemplateConfigurationProperties templateConfigurationProperties) {
        templateFunctionInitializer = new TemplateFunctionInitializer(templateConfigurationProperties);
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

    @Override
    public void resetFunctionTemplates() {
        templateFunctions.clear();
        templateFunctions.addAll(templateFunctionInitializer.initTemplateFunction());
    }
}
