package anton.skripin.development.eumcf.controller;

import anton.skripin.development.domain.model.ModelElement;
import anton.skripin.development.eumcf.gateway.api.ExternalModelIntegrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ConstraintController {

    private final ExternalModelIntegrationService modelIntegrationService;

    public ConstraintController(ExternalModelIntegrationService modelIntegrationService) {
        this.modelIntegrationService = modelIntegrationService;
    }

    @GetMapping("${main-view.uri}")
    public String displayMainConstraintView(Model model, @RequestParam("id") String id) {
        ModelElement modelElement = modelIntegrationService.getModelElementById(id);
        return null;
    }
}
