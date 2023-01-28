package anton.skripin.development.service.api;

import anton.skripin.development.domain.template.Template;

public interface ConstraintDefinitionService {
    Template getConstraintTemplate();

    Template getConstraintTemplate(String targetUuid, String targetType);
}
