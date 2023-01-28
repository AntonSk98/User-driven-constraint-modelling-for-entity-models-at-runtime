package anton.skripin.development.eumcf.simulation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ModelRegistry {

    List<ModelElement> modelElements;

    @Getter
    @AllArgsConstructor
    public static class ModelElement {
        String id;
        String name;
        boolean isTemplate;
        List<ParentRelation> parentRelations;
        List<Attribute> attributes;
        List<Association> associations;
    }

    @Getter
    @AllArgsConstructor
    public static class ParentRelation {
        String id;
        String name;
    }

    @Getter
    @AllArgsConstructor
    public class Attribute {
        String name;
        String datatype;
        boolean nonEmpty;
    }

    @Getter
    @AllArgsConstructor
    public class Association {
        String name;
        String target;
        String multiplicity;
    }
}
