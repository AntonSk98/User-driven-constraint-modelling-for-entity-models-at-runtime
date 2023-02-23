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

package ansk.development.eumcf.controller;

import ansk.development.domain.model.ModelElement;
import ansk.development.eumcf.service.api.InstanceService;
import ansk.development.eumcf.service.api.ModelService;
import ansk.development.service.api.ConstraintDefinitionService;
import ansk.development.service.api.ConstraintPersistenceService;
import ansk.development.service.api.TemplateFunctionService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ModelAndViewController {

    private final ModelService modelService;
    private final InstanceService instanceService;

    private final ConstraintPersistenceService constraintPersistenceService;

    private final TemplateFunctionService templateFunctionService;

    private final ConstraintDefinitionService constraintDefinitionService;

    private final Environment environment;


    public ModelAndViewController(ModelService modelService,
                                  InstanceService instanceService,
                                  ConstraintPersistenceService constraintPersistenceService,
                                  TemplateFunctionService templateFunctionService,
                                  ConstraintDefinitionService constraintDefinitionService, Environment environment) {
        this.modelService = modelService;
        this.instanceService = instanceService;
        this.constraintPersistenceService = constraintPersistenceService;
        this.templateFunctionService = templateFunctionService;
        this.constraintDefinitionService = constraintDefinitionService;
        this.environment = environment;
    }

    /**
     * Constructs the main page.
     *
     * @return html main page with model and instance space
     */
    @GetMapping("/")
    public ModelAndView displayAvailableModelsAndInstances() {
        Map<String, Object> params = new HashMap<>();
        params.put("implementation", determineConstraintImplementation());
        params.put("models", modelService.getAllModelElements());
        params.put("typeToInstances", instanceService.getTypeToInstancesMap());
        params.put("constraintsByType", constraintPersistenceService.getGroupConstraintsByTypeAnsSupertypes(
                modelService.getAllTypesAndTheirSupertypesMap()
        ));
        return new ModelAndView("main_view", params);
    }

    /**
     * Construct a 'create-instance' page.
     *
     * @param id   of an instance
     * @param name of a model element
     * @return html page for instance creation
     */
    @GetMapping("/create_instance")
    public ModelAndView createInstanceView(
            @RequestParam String id,
            @RequestParam String name
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("modelElement", modelService.getModelElementByIdAndName(id, name));
        params.put("update", false);
        return new ModelAndView("instance_view", params);
    }

    /**
     * Construct an 'update-instance' page.
     *
     * @param id of an instance
     * @return html page to update instance
     */
    @GetMapping("/update_instance")
    public ModelAndView updateInstanceView(
            @RequestParam String id
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("instance", instanceService.withAssociations(instanceService.getInstanceByUuid(id)));
        params.put("update", true);
        return new ModelAndView("instance_view", params);
    }

    /**
     * Construct the page with an editor to define constraints on a model element.
     *
     * @param id   of a model element
     * @param name of a model element
     * @return html page to define constraints
     */
    @GetMapping("/constrain_model_element")
    public ModelAndView constrainModelElement(@RequestParam("id") String id, @RequestParam("name") String name) {
        return new ModelAndView("model_element_view", getConstraintViewParams(id, name, false));
    }

    /**
     * Construct the page to update model element.
     *
     * @param id   of a model element
     * @param name of a model element
     * @return html page to update a model element
     */
    @GetMapping("/update_model_element")
    public ModelAndView updateModelElement(@RequestParam("id") String id, @RequestParam("name") String name) {
        return new ModelAndView("model_element_view", getConstraintViewParams(id, name, true));
    }

    private Map<String, Object> getConstraintViewParams(String id, String name, boolean updateMode) {
        ModelElement modelElement = modelService.getModelElementByIdAndName(id, name);
        Map<String, Object> params = new HashMap<>();
        params.put("modelElement", modelElement);
        params.put("functions", templateFunctionService.getAllTemplates());
        params.put("constraintTemplate", constraintDefinitionService.getConstraintTemplate(modelElement.getUuid(), modelElement.getName()));
        params.put("update", updateMode);
        return params;
    }

    private String determineConstraintImplementation() {
        List<String> activeProfiles = List.of(this.environment.getActiveProfiles());
        if (activeProfiles.contains("gremlin")) {
            return "GREMLIN";
        }
        if (activeProfiles.contains("shacl")) {
            return "SHACL";
        }
        return "ABSTRACT";
    }
}
