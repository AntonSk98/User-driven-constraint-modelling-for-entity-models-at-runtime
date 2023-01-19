package anton.skripin.development.service;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.ViolationLevel;
import anton.skripin.development.domain.template.Template;
import anton.skripin.development.exception.ConstraintParsingException;
import anton.skripin.development.exception.ConstraintTemplateCreationException;
import anton.skripin.development.mapper.AbstractToPSConstraintMapper;
import anton.skripin.development.properties.TemplateConfigurationProperties;
import anton.skripin.development.registry.AbstractToPSConstraintMapperRegistry;
import anton.skripin.development.service.api.ConstraintDefinitionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class ConstraintDefinitionServiceImpl implements ConstraintDefinitionService {

    private final TemplateConfigurationProperties properties;
    private final AbstractToPSConstraintMapperRegistry abstractToPSConstraintMapperRegistry;
    private final ObjectMapper objectMapper;

    public ConstraintDefinitionServiceImpl(TemplateConfigurationProperties properties, AbstractToPSConstraintMapper constraintMapper) {
        this.abstractToPSConstraintMapperRegistry = new AbstractToPSConstraintMapperRegistry(constraintMapper);
        this.objectMapper = new ObjectMapper();
        this.properties = properties;
    }

    @Override
    public Template createConstraintTemplate() {
        Constraint constraint = new Constraint();
        constraint.setUuid(UUID.randomUUID().toString());
        constraint.setName(properties.getPlaceholder());
        constraint.setViolationLevel(ViolationLevel.ERROR);
        constraint.setViolationMessage(properties.getPlaceholder());
        constraint.setTargetModelElementId(properties.getPlaceholder());
        constraint.setTargetModelElementName(properties.getPlaceholder());
        constraint.setConstraintFunction(null);
        try {
            String constraintTemplate = objectMapper
                    .writeValueAsString(constraint)
                    .replace("null", properties.getPlaceholder());
            return Template.of(constraintTemplate);
        } catch (JsonProcessingException e) {
            throw new ConstraintTemplateCreationException();
        }
    }

    @Override
    public String processConstraint(Template template) {
        try {
            Constraint userDefinedConstraint = objectMapper.readValue(template.getTemplate(), Constraint.class);
            return abstractToPSConstraintMapperRegistry.mapToPlatformSpecifConstraint(userDefinedConstraint);
        } catch (JsonProcessingException e) {
            throw new ConstraintParsingException(template.getUuid());
        }
    }
}
