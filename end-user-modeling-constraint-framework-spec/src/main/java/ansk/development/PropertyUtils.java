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

package ansk.development;

import ansk.development.domain.constraint.functions.FunctionDescription;
import ansk.development.properties.DefaultTemplateConfigurationProperties;

import java.io.IOException;
import java.util.Properties;

/**
 * Utility class to read external properties.
 */
public class PropertyUtils {

    /**
     * Gets default properties for {@link FunctionDescription}.
     *
     * @return {@link Properties}
     */
    public static Properties getFunctionToDescriptionProperties() {
        return getPropertiesByFileName("/properties/function_to_description.properties");
    }


    /**
     * Gets default placeholders for {@link DefaultTemplateConfigurationProperties}.
     *
     * @return
     */
    public static Properties getPlaceholderProperties() {
        return getPropertiesByFileName("/properties/placeholder.properties");
    }

    private static Properties getPropertiesByFileName(String filename) {
        Properties properties = new Properties();
        try {
            properties.load(PropertyUtils.class.getResourceAsStream(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
