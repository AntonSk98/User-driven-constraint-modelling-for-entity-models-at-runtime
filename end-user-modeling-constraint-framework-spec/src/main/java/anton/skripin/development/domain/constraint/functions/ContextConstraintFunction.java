package anton.skripin.development.domain.constraint.functions;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Function applied to one attribute.
 * It has a path to an attribute and parameters.
 */
@Getter
@Setter
public abstract class ContextConstraintFunction extends ConstraintFunction {
    private String path;
    private Map<String, String> params;

    /**
     * Constructor.
     *
     * @param name   of a function
     * @param path   to an attribute
     * @param params parameters
     */
    public ContextConstraintFunction(String name, String path, Map<String, String> params) {
        super(name);
        this.path = path;
        this.params = Objects.isNull(params) ? Collections.emptyMap() : params;
    }
}
