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
import ansk.development.properties.TemplateConfigurationProperties;
import ansk.development.service.api.ConstraintDefinitionService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConstraintDefinitionServiceImpl implements ConstraintDefinitionService {

    private final TemplateConfigurationProperties properties;
    private final ObjectMapper objectMapper;
    TemplateFunctionInitializer templateFunctionInitializer;

    public ConstraintDefinitionServiceImpl(TemplateConfigurationProperties properties) {
        this.templateFunctionInitializer = new TemplateFunctionInitializer(properties);
        this.objectMapper = new ObjectMapper();
        this.properties = properties;
    }

    @Override
    public Template getConstraintTemplate() {
        return getConstraintTemplate(null, null);
    }

    @Override
    public Template getConstraintTemplateWithUuid(String modelElementUuid) {
        return getConstraintTemplate(modelElementUuid, null);
    }

    @Override
    public Template getConstraintTemplateWithType(String modelElementType) {
        return getConstraintTemplate(null, modelElementType);
    }

    @Override
    public Template getConstraintTemplate(String modelElementUuid, String modelElementType) {
        return templateFunctionInitializer.getConstraintTemplate(modelElementUuid, modelElementType);
    }

    @Override
    public Template getRuntimeFunctionTemplate() {
        return templateFunctionInitializer.getRuntimeFunctionTemplate();
    }
}
