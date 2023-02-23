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

import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.ConstraintValidationReport;
import ansk.development.domain.constraint.functions.FunctionType;
import ansk.development.domain.constraint.functions.types.RuntimeFunction;
import ansk.development.domain.instance.InstanceElement;
import ansk.development.domain.template.Template;
import ansk.development.eumcf.service.api.InstanceService;
import ansk.development.exception.constraint.PersistConstraintException;
import ansk.development.service.api.ConstraintDefinitionService;
import ansk.development.service.api.ConstraintPersistenceService;
import ansk.development.service.api.ConstraintValidationService;
import ansk.development.service.api.TemplateFunctionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

/**
 * Rest controller to manage constraints.
 */
@RestController
public class ConstraintController {

    private final ConstraintPersistenceService constraintPersistenceService;

    private final ConstraintValidationService constraintValidationService;

    private final InstanceService instanceService;
    private final TemplateFunctionService templateFunctionService;
    private final ConstraintDefinitionService constraintDefinitionService;


    /**
     * Constructor.
     *
     * @param constraintPersistenceService See {@link ConstraintPersistenceService}
     * @param constraintValidationService  See {@link ConstraintValidationService}
     * @param instanceService              See {@link InstanceService}
     * @param templateFunctionService      See {@link TemplateFunctionService}
     * @param constraintDefinitionService  See {@link ConstraintDefinitionService}
     */
    public ConstraintController(ConstraintPersistenceService constraintPersistenceService, ConstraintValidationService constraintValidationService, InstanceService instanceService, TemplateFunctionService templateFunctionService, ConstraintDefinitionService constraintDefinitionService) {
        this.constraintPersistenceService = constraintPersistenceService;
        this.constraintValidationService = constraintValidationService;
        this.instanceService = instanceService;
        this.templateFunctionService = templateFunctionService;
        this.constraintDefinitionService = constraintDefinitionService;
    }

    /**
     * Fetches a constraint by a given uuid.
     *
     * @param uuid uuid of a persisted constraint
     * @return retrieved {@link Constraint}
     */
    @GetMapping("/get_constraint_by_id")
    public Constraint getConstraintById(@RequestParam String uuid) {
        return constraintPersistenceService.getConstraintByUuid(uuid);
    }

    /**
     * Removes a constraint by a given uuid
     *
     * @param uuid uuid of a persisted constraint
     * @return true if the constraint is removed. Otherwise, false
     */
    @GetMapping("/remove_constraint_by_id")
    public boolean removeConstraintById(@RequestParam String uuid) {
        return constraintPersistenceService.removeConstraintByUuid(uuid);
    }

    /**
     * Validates a constraint by a given uuid.
     *
     * @param instanceUuid   instanceUuid
     * @param constraintUuid uuid of a persisted constraint
     * @return todo return a domain object
     */
    @GetMapping("/validate_constraint_by_id")
    public ConstraintValidationReport validateConstraintById(@RequestParam String instanceUuid, @RequestParam String constraintUuid) {
        Constraint constraint = constraintPersistenceService.getConstraintByUuid(constraintUuid);
        Set<Set<String>> requiredSubgraphElements = constraintValidationService.getRequiredSubgraphElements(constraint);
        List<InstanceElement> instanceGraph = instanceService.getRequiredSubgraph(instanceUuid, requiredSubgraphElements);
        return constraintValidationService.validateConstraint(instanceUuid, instanceGraph, constraint);
    }


    /**
     * Saves a given constraint. To check the conformance of to its domain specification,
     * it is first explicitly parsed to {@link Constraint} and only then passes to the respective service method
     *
     * @param typeName   model element name. E.g. <Car>, <House>, etc.
     * @param constraint passed constraint in a String format.
     * @return true if constraint is successfully saved
     * @throws PersistConstraintException if error occurred while validating the syntax of a constraint according to the {@link Constraint} specification
     */
    @PostMapping("/persist_constraint")
    public boolean saveConstraint(@RequestParam String typeName, @RequestBody String constraint) {
        try {
            Constraint unmarshalled = new ObjectMapper().readValue(constraint, Constraint.class);
            boolean isSaved = constraintPersistenceService.saveConstraint(typeName, unmarshalled);
            if (!isSaved) {
                return false;
            }
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Constraint does not adhere to its specification", e);
        }
        return true;
    }


    @GetMapping("/get_runtime_function_template")
    public Template getRuntimeFunctionTemplate() {
        return constraintDefinitionService.getRuntimeFunctionTemplate();
    }

    @PostMapping("/save_runtime_function")
    public void addRuntimeFunctionTemplate(@RequestBody RuntimeFunction runtimeFunction) {
        try {
            templateFunctionService.addNewTemplate(runtimeFunction.getName(), "", FunctionType.RUNTIME_FUNCTION, new ObjectMapper().writeValueAsString(runtimeFunction));
        } catch (JsonProcessingException e) {
            throw new PersistConstraintException("Error occurred while adding a constraint function template", e);
        }
    }

    @GetMapping("/reset_function_templates")
    public void resetFunctionTemplates() {
        templateFunctionService.resetFunctionTemplates();
    }

    @GetMapping("/get_template")
    public Template getTemplateByName(@RequestParam String name) {
        return templateFunctionService.getTemplateByFunctionName(name);
    }
}
