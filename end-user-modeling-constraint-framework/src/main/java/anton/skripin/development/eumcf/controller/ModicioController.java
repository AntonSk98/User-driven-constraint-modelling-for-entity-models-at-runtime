package anton.skripin.development.eumcf.controller;


import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModicioController {
    @GetMapping("/update_attribute")
    @SneakyThrows
    public boolean updateAttribute() {
        return true;
    }

    @GetMapping("/update_association")
    @SneakyThrows
    public boolean updateAssociation() {
        return true;
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
