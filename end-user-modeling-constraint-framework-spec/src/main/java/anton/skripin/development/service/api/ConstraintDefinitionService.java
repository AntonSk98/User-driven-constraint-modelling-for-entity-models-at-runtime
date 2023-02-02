package anton.skripin.development.service.api;

import anton.skripin.development.domain.template.Template;

/**
 * Exposes functions to define constraints.
 */
public interface ConstraintDefinitionService {
    /**
     * Provides a template {@link Template} for external services.
     *
     * @return {@link Template}
     */
    Template getConstraintTemplate();

    /**
     * Provides {@link Template} for external services with predefined uuid of a model element.
     *
     * @param modelElementUuid uuid of a model element
     * @return {@link Template}
     */
    Template getConstraintTemplateWithUuid(String modelElementUuid);

    /**
     * Provides {@link Template} for external services with predefined type of model element.
     *
     * @param modelElementType type of model element
     * @return {@link Template}
     */
    Template getConstraintTemplateWithType(String modelElementType);

    /**
     * Provides {@link Template} for external services with predefined uuid and type of model element.
     *
     * @param modelElementUuid uuid of model element
     * @param modelElementType type of model element
     * @return
     */
    Template getConstraintTemplate(String modelElementUuid, String modelElementType);
}
