package anton.skripin.development.eumcf.controller;

import ansk.development.mapper.GremlinConstraintMapper;
import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.functions.FunctionType;
import anton.skripin.development.domain.constraint.functions.types.RuntimeFunction;
import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.domain.template.Template;
import anton.skripin.development.eumcf.service.api.InstanceService;
import anton.skripin.development.exception.constraint.PersistConstraintException;
import anton.skripin.development.service.api.ConstraintDefinitionService;
import anton.skripin.development.service.api.ConstraintPersistenceService;
import anton.skripin.development.service.api.ConstraintValidationService;
import anton.skripin.development.service.api.TemplateFunctionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

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
    public boolean validateConstraintById(@RequestParam String instanceUuid, @RequestParam String constraintUuid) {
        Constraint constraint = constraintPersistenceService.getConstraintByUuid(constraintUuid);
        Set<Set<String>> requiredSubgraphElements = constraintValidationService.getRequiredSubgraphElements(constraint);
        GremlinConstraintMapper gremlinConstraintMapper = new GremlinConstraintMapper();
        List<InstanceElement> instanceGraph = instanceService.getRequiredSubgraph(instanceUuid, requiredSubgraphElements);
        gremlinConstraintMapper.mapToPlatformSpecificGraph(instanceGraph);
        constraintValidationService.validateConstraint(instanceUuid, instanceGraph, constraint);
        System.out.println("It should be changed later!");
        return true;
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
            throw new PersistConstraintException("Error occurred while persisting a constraint", e);
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
