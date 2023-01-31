package anton.skripin.development.eumcf.controller;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.domain.model.ModelElement;
import anton.skripin.development.eumcf.service.InstanceSpaceService;
import anton.skripin.development.eumcf.service.ModelSpaceService;
import anton.skripin.development.service.api.ConstraintDefinitionService;
import anton.skripin.development.service.api.ConstraintPersistenceService;
import anton.skripin.development.service.api.TemplateFunctionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ConstraintEngineController {

    private final ModelSpaceService modelSpaceService;
    private final InstanceSpaceService instanceSpaceService;

    private final ConstraintPersistenceService constraintPersistenceService;
    private final TemplateFunctionService templateFunctionService;

    private final ConstraintDefinitionService constraintDefinitionService;

    public ConstraintEngineController(ModelSpaceService modelSpaceService,
                                      InstanceSpaceService instanceSpaceService,
                                      ConstraintPersistenceService constraintPersistenceService,
                                      TemplateFunctionService templateFunctionService,
                                      ConstraintDefinitionService constraintDefinitionService) {
        this.modelSpaceService = modelSpaceService;
        this.instanceSpaceService = instanceSpaceService;
        this.constraintPersistenceService = constraintPersistenceService;
        this.templateFunctionService = templateFunctionService;
        this.constraintDefinitionService = constraintDefinitionService;
    }

    @SneakyThrows
    @GetMapping("/")
    public ModelAndView displayAvailableModelsAndInstances() {
        Map<String, Object> params = new HashMap<>();
        params.put("models", modelSpaceService.getAllModelElements());
        params.put("typeToInstances", instanceSpaceService.getTypeToInstancesMap());
        params.put("constraintsByType", constraintPersistenceService.getGroupConstraintsByTypeAnsSupertypes(
                modelSpaceService.getAllTypesAndTheirSupertypesMap()
        ));
        return new ModelAndView("main_view", params);
    }

    @SneakyThrows
    @GetMapping("/create_instance")
    public ModelAndView createInstanceView(
            @RequestParam String id,
            @RequestParam String name
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("modelElement", modelSpaceService.getModelElementByIdAndName(id, name));
        params.put("update", false);
        return new ModelAndView("instance_view", params);
    }

    @SneakyThrows
    @GetMapping("/update_instance")
    public ModelAndView updateInstanceView(
            @RequestParam String id
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("instance", instanceSpaceService.withAssociations(instanceSpaceService.getInstanceById(id)));
        params.put("update", true);
        return new ModelAndView("instance_view", params);
    }

    @SneakyThrows
    @GetMapping("/constrain_model_element")
    public ModelAndView constrainModelElement(@RequestParam("id") String id, @RequestParam("name") String name) {
        return new ModelAndView("model_element_view", getConstraintViewParams(id, name, false));
    }

    @SneakyThrows
    @GetMapping("/update_model_element")
    public ModelAndView updateModelElement(@RequestParam("id") String id, @RequestParam("name") String name) {
        return new ModelAndView("model_element_view", getConstraintViewParams(id, name, true));
    }


    private Map<String, Object> getConstraintViewParams(String id, String name, boolean updateMode) {
        ModelElement modelElement = modelSpaceService.getModelElementByIdAndName(id, name);
        Map<String, Object> params = new HashMap<>();
        params.put("modelElement", modelElement);
        params.put("functions", templateFunctionService.getAllTemplates());
        params.put("constraintTemplate", constraintDefinitionService.getConstraintTemplate(modelElement.getUuid(), modelElement.getName()));
        params.put("update", updateMode);
        return params;
    }

    @GetMapping("/add_to_opened_model_element")
    @SneakyThrows
    public ResponseEntity<ModelElement> addToOpenedModelElement(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("path") String path
    ) {
        return ResponseEntity.ok().body(modelSpaceService.addToOpenedModelElement(from, to, path));
    }

    @GetMapping("/get_instance_by_id")
    @SneakyThrows
    public ResponseEntity<InstanceElement> getInstanceById(@RequestParam String uuid) {
        return ResponseEntity.ok().body(instanceSpaceService.getInstanceById(uuid));
    }

    @GetMapping("/get_constraint_by_id")
    @SneakyThrows
    public ResponseEntity<Constraint> getConstraintById(@RequestParam String uuid) {
        return ResponseEntity.ok().body(constraintPersistenceService.getConstraintByUuid(uuid));
    }

    @GetMapping("/remove_constraint_by_id")
    @SneakyThrows
    public ResponseEntity<Boolean> removeConstraintById(@RequestParam String uuid) {
        return ResponseEntity.ok().body(constraintPersistenceService.removeConstraintByUuid(uuid));
    }

    @GetMapping("/validate_constraint_by_id")
    @SneakyThrows
    public ResponseEntity<Boolean> validateConstraintById(@RequestParam String uuid) {
        Constraint constraint = constraintPersistenceService.getConstraintByUuid(uuid);
        System.out.println("It should be changed later!");
        return ResponseEntity.ok().body(true);
    }


    @PostMapping("/persist_constraint")
    public ResponseEntity<String> saveConstraint(@RequestParam String typeName, @RequestBody String constraint) {
        try {
            Constraint deserealizedConstraint = new ObjectMapper().readValue(constraint, Constraint.class);
            boolean isSaved = constraintPersistenceService.saveConstraint(typeName, deserealizedConstraint);
            if (!isSaved) {
                return ResponseEntity.internalServerError().body("Failed to save the constraint");
            }
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body("Submitted constraint violates the specification");
        }
        return ResponseEntity.ok().body("Constraint has been successfully saved");
    }
}
