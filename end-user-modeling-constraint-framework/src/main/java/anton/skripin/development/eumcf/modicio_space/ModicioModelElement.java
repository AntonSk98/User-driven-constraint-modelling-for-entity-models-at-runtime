package anton.skripin.development.eumcf.modicio_space;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ModicioModelElement {
    String id;
    String name;
    boolean isTemplate;
    List<ModicioParentRelation> parentRelations;
    List<ModicioAttribute> attributes;
    List<ModicioAssociation> associations;
}
