package anton.skripin.development.service.api;

import anton.skripin.development.domain.template.Template;

public interface ConstraintDefinitionService {
    Template createConstraintTemplate();

    String processConstraint(Template template);
}
