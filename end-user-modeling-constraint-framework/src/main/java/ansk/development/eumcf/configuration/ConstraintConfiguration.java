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


import ansk.development.eumcf.mapper.ModicioInstanceMapper;
import ansk.development.eumcf.mapper.ModicioModelMapper;
import ansk.development.mapper.InstanceMapper;
import ansk.development.mapper.ModelMapper;
import ansk.development.properties.TemplateConfigurationProperties;
import ansk.development.service.ConstraintDefinitionServiceImpl;
import ansk.development.service.SimpleConstraintPersistenceService;
import ansk.development.service.SimpleTemplateFunctionService;
import ansk.development.service.api.ConstraintDefinitionService;
import ansk.development.service.api.ConstraintPersistenceService;
import ansk.development.service.api.TemplateFunctionService;
import modicio.core.DeepInstance;
import modicio.core.ModelElement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for constraint specification necessary implementation classes.
 */
@Configuration
public class ConstraintConfiguration {

    /**
     * Provides concrete implementation for {@link InstanceMapper<DeepInstance>} in the context of Modicio.
     *
     * @return {@link InstanceMapper<DeepInstance>}
     */
    @Bean
    public InstanceMapper<DeepInstance> instanceMapper() {
        return new ModicioInstanceMapper();
    }

    /**
     * Provides concrete implementation for {@link ModelMapper<ModelElement>} in the context of Modicio.
     *
     * @return {@link ModelMapper<ModelElement>}
     */
    @Bean
    public ModelMapper<ModelElement> modelMapper() {
        return new ModicioModelMapper();
    }


    /**
     * Provides constraint validation service to manage constraints.
     *
     * @return {@link SimpleConstraintPersistenceService}
     */
    @Bean
    public ConstraintPersistenceService constraintPersistenceService() {
        return new SimpleConstraintPersistenceService();
    }

    /**
     * Provides a service with all available constraint templates to be exposed to end-users.
     *
     * @param properties dynamic properties configured in {@link  TemplateConfigurationProperties}
     * @return {@link SimpleTemplateFunctionService}
     */
    @Bean
    public TemplateFunctionService templateFunctionService(TemplateConfigurationProperties properties) {
        return new SimpleTemplateFunctionService(properties);
    }

    /**
     * Provides a service to define constraints.
     *
     * @param properties dynamic properties configured in {@link  TemplateConfigurationProperties}
     * @return {@link ConstraintDefinitionServiceImpl}
     */
    @Bean
    public ConstraintDefinitionService constraintDefinitionService(TemplateConfigurationProperties properties) {
        return new ConstraintDefinitionServiceImpl(properties);
    }

}
