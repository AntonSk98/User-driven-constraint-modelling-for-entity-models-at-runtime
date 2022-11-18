package de.antonsk98.development.gremlin.domain.constraints;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.antonsk98.development.gremlin.domain.constraints.functions.Function;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Constraint {
    private final String focusProperty;
    private final String message;
    private final ConstraintViolationSeverity severity;
    private final List<Function> functions;

    @JsonCreator
    public Constraint(
            @JsonProperty("focusProperty") String focusProperty,
            @JsonProperty("message") String message,
            @JsonProperty("severity") ConstraintViolationSeverity severity,
            @JsonProperty("functions") List<Function> functions) {
        this.focusProperty = focusProperty;
        this.message = message;
        this.severity = severity;
        this.functions = functions;
    }

    public String getFocusProperty() {
        return focusProperty;
    }

    public String getMessage() {
        return message;
    }

    public ConstraintViolationSeverity getSeverity() {
        return severity;
    }

    public List<Function> getFunctions() {
        return functions;
    }
}
