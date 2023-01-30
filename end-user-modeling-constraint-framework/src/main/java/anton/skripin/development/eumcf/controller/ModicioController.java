package anton.skripin.development.eumcf.controller;


import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.domain.instance.Link;
import anton.skripin.development.domain.instance.Slot;
import anton.skripin.development.eumcf.modicio_space.service.ModicioService;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/delete_instance_by_id")
    @SneakyThrows
    public void deleteInstanceById(String uuid) {
        modicioService.deleteInstanceById(uuid);
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

    @PostMapping("/instantiate_element")
    public void createInstance(@RequestBody InstanceElement instanceElement) {
        modicioService.createInstance(instanceElement.getInstanceOf(), instanceElement.getSlots(), instanceElement.getLinks());
    }

    @PostMapping("/update_element")
    public void updateInstance(@RequestBody InstanceElement instanceElement) {
        modicioService.updateInstance(instanceElement.getUuid(), instanceElement.getSlots(), instanceElement.getLinks());
    }
}
