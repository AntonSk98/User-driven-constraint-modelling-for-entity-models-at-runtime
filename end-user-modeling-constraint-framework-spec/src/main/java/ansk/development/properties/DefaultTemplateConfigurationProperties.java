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

import ansk.development.PropertyUtils;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Properties;

/**
 * Provides default placeholders for {@link TemplateConfigurationProperties}.
 */
@Getter
public class DefaultTemplateConfigurationProperties extends TemplateConfigurationProperties {

    Properties properties = PropertyUtils.getPlaceholderProperties();

    @Override
    public String attributePlaceholder() {
        return properties.getProperty("ATTRIBUTE");
    }

    @Override
    public String attributePlaceholderWithValidation() {
        return properties.getProperty("ATTRIBUTE_WITH_NAVIGATION");
    }

    @Override
    public String navigationPlaceholder() {
        return properties.getProperty("NAVIGATION");
    }

    @Override
    public String paramValuePlaceholder() {
        return properties.getProperty("PARAMETERS");
    }

    @Override
    public String arrayValuePlaceholder() {
        return properties.getProperty("ARRAY_VALUE");
    }

    @Override
    public ImmutablePair<String, String> objectKeyValuePlaceholder() {
        return new ImmutablePair<>(properties.getProperty("OBJECT_KEY"), properties.getProperty("OBJECT_VALUE"));
    }

    @Override
    public String constraintNamePlaceholder() {
        return properties.getProperty("CONSTRAINT_NAME");
    }

    @Override
    public String violationMessagePlaceholder() {
        return properties.getProperty("VIOLATION_MESSAGE");
    }

    @Override
    public String runtimeFunctionPlaceholder() {
        return properties.getProperty("RUNTIME_FUNCTION_PLACEHOLDER");
    }
}
