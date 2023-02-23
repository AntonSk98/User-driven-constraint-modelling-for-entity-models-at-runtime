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

package ansk.development.properties;

import ansk.development.domain.template.Template;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * Class representing properties for a {@link Template}
 */
public abstract class TemplateConfigurationProperties {

    /**
     * Default constructor.
     */
    public TemplateConfigurationProperties() {
    }

    /**
     * Placeholder value used as a template for a single-valued attribute that must be replaced by an end-user.
     */
    public abstract String attributePlaceholder();

    public abstract String attributePlaceholderWithValidation();

    /**
     * Placeholder value used as a template for a navigation that must be replaced by an end-user.
     */
    public abstract String navigationPlaceholder();

    /**
     * Placeholder for a function parameter that must be replaced by an end-user.
     */
    public abstract String paramValuePlaceholder();

    /**
     * Placeholder for an array that accepts several functions.
     */
    public abstract String arrayValuePlaceholder();

    /**
     * Placeholder for an object key-value pair that must be replaced by an end-user.
     */
    public abstract ImmutablePair<String, String> objectKeyValuePlaceholder();

    /**
     * Every constraint must be provided with a name by end-user by replacing a placeholder.
     */
    public abstract String constraintNamePlaceholder();

    /**
     * Every constraint must have a meaningful message that will be returned in case of a constraint violation.
     */
    public abstract String violationMessagePlaceholder();

    public abstract String runtimeFunctionPlaceholder();
}
