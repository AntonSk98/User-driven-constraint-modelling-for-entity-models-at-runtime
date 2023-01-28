package anton.skripin.development.service;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.ViolationLevel;
import anton.skripin.development.domain.template.Template;
import anton.skripin.development.exception.ConstraintTemplateCreationException;
import anton.skripin.development.properties.TemplateConfigurationProperties;
import anton.skripin.development.service.api.ConstraintDefinitionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class ConstraintDefinitionServiceImpl implements ConstraintDefinitionService {

    private final TemplateConfigurationProperties properties;
    private final ObjectMapper objectMapper;

    public ConstraintDefinitionServiceImpl(TemplateConfigurationProperties properties) {
        this.objectMapper = new ObjectMapper();
        this.properties = properties;
    }

    @Override
    public Template getConstraintTemplate() {
        return getConstraintTemplate(properties.getSimplePlaceholder(), properties.getSimplePlaceholder());
    }

    @Override
    public Template getConstraintTemplate(String targetUuid, String targetType) {
        Constraint constraint = new Constraint();
        constraint.setUuid(UUID.randomUUID().toString());
        constraint.setName(properties.getSimplePlaceholder());
        constraint.setViolationLevel(ViolationLevel.ERROR);
        constraint.setViolationMessage(properties.getSimplePlaceholder());
        constraint.setTargetModelElementId(targetUuid);
        constraint.setTargetModelElementName(targetType);
        constraint.setConstraintFunction(null);
        try {
            String constraintTemplate = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(constraint)
                    .replace("null", String.format("{\"%s\": \"%s\"}",
                            properties.getObjectPlaceholder().getLeft(),
                            properties.getObjectPlaceholder().getRight()));
            return Template.ofConstraint(constraintTemplate);
        } catch (JsonProcessingException e) {
            throw new ConstraintTemplateCreationException();
        }
    }
}
