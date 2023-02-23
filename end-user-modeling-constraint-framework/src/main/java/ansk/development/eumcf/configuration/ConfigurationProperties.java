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

package ansk.development.eumcf.configuration;

import ansk.development.properties.DefaultTemplateConfigurationProperties;
import ansk.development.properties.TemplateConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration file for dynamic properties of a constraint specification.
 */
@Configuration
public class ConfigurationProperties {

    /**
     * Bean for {@link TemplateConfigurationProperties}.
     *
     * @return {@link TemplateConfigurationProperties}
     */
    @Bean
    public TemplateConfigurationProperties templateConfigurationProperties() {
        return new DefaultTemplateConfigurationProperties();
    }
}
