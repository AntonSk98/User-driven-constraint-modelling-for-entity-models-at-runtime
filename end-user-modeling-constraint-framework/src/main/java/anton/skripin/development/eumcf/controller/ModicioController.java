package anton.skripin.development.eumcf.controller;


import anton.skripin.development.eumcf.modicio_space.service.ModicioService;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModicioController {

    private final ModicioService modicioService;

    public ModicioController(ModicioService modicioService) {
        this.modicioService = modicioService;
    }

    @GetMapping("/update_attribute")
    @SneakyThrows
    public void updateAttribute(
            @RequestParam String id,
            @RequestParam String type,
            @RequestParam String attributeId,
            @RequestParam String key,
            @RequestParam String datatype
    ) {
        modicioService.updateAttributeDefinition(id, type, attributeId, key, datatype);
    }

    @GetMapping("/update_association")
    @SneakyThrows
    public void updateAssociation(
            @RequestParam String id,
            @RequestParam String type,
            @RequestParam String associationId,
            @RequestParam String byRelation,
            @RequestParam String target
    ) {
        modicioService.updateAssociationDefinition(id, type, associationId, byRelation, target);
    }

    @GetMapping("/update_slot")
    @SneakyThrows
    public boolean updateSlot() {
        return true;
    }

    @GetMapping("/update_link")
    public boolean updateLink() {
        return true;
    }
}
