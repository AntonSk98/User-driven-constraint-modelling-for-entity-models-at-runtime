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

package ansk.development.service.api;

import ansk.development.domain.template.Template;

/**
 * Exposes functions to define constraints.
 */
public interface ConstraintDefinitionService {
    /**
     * Provides a template {@link Template} for external services.
     *
     * @return {@link Template}
     */
    Template getConstraintTemplate();

    /**
     * Provides {@link Template} for external services with predefined uuid of a model element.
     *
     * @param modelElementUuid uuid of a model element
     * @return {@link Template}
     */
    Template getConstraintTemplateWithUuid(String modelElementUuid);

    /**
     * Provides {@link Template} for external services with predefined type of model element.
     *
     * @param modelElementType type of model element
     * @return {@link Template}
     */
    Template getConstraintTemplateWithType(String modelElementType);

    /**
     * Provides {@link Template} for external services with predefined uuid and type of model element.
     *
     * @param modelElementUuid uuid of model element
     * @param modelElementType type of model element
     * @return {@link Template}
     */
    Template getConstraintTemplate(String modelElementUuid, String modelElementType);

    /**
     * Provides {@link Template} for runtime function.
     *
     * @return {@link Template}
     */
    Template getRuntimeFunctionTemplate();
}
