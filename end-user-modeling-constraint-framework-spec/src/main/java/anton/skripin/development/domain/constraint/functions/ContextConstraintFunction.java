package anton.skripin.development.domain.constraint.functions;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public abstract class ContextConstraintFunction extends ConstraintFunction {
    private String path;
    private Map<String, String> params;

    public ContextConstraintFunction(String name, String path, Map<String, String> params) {
        super(name);
        this.path = path;
        this.params = Objects.isNull(params) ? Collections.emptyMap() : params;
    }
}
