package ansk.development.domain.template;

import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.functions.ConstraintFunction;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Represents a template bag that can be either
 * {@link ConstraintFunction}
 * or
 * {@link Constraint}.
 * <p>
 * Every template is a predefined instance with default values.
 * <p>
 * To facilitate the process of constraint definition by end-users,
 * most of the static values are filled out automatically.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Template {
    private String uuid;
    private String functionName;
    private String functionType;

    private String description;
    private String template;

    private Template(String template) {
        this.template = template;
    }

    private Template(String functionName, String description, String functionType, String template) {
        this.uuid = UUID.randomUUID().toString();
        this.functionName = functionName;
        this.description = description;
        this.functionType = functionType;
        this.template = template;
    }

    public static Template ofFunction(String functionName, String description, String functionType, String template) {
        return new Template(functionName, description, functionType, template);
    }

    public static Template ofConstraint(String template) {
        return new Template(template);
    }
}