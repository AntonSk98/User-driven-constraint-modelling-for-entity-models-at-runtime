package anton.skripin.development.domain.template;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Template {
    private String uuid;
    private String functionName;
    private String functionType;
    private String template;

    private Template(String template) {
        this.template = template;
    }

    private Template(String functionName, String functionType, String template) {
        this.uuid = UUID.randomUUID().toString();
        this.functionName = functionName;
        this.functionType = functionType;
        this.template = template;
    }

    public static Template of(String functionName, String functionType, String template) {
        return new Template(functionName, functionType, template);
    }

    public static Template of(String template) {
        return new Template(template);
    }
}
