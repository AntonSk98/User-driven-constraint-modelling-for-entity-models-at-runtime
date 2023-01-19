package anton.skripin.development.mapper;

import anton.skripin.development.domain.constraint.Constraint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SimpleAbstractToPSConstraintMapper implements AbstractToPSConstraintMapper {

    @Override
    public String mapToPlatformSpecificConstraint(Constraint constraint) {
        try {
            return new ObjectMapper().writeValueAsString(constraint);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error occurred while mapping abstract to platform-specific constraint!");
        }
    }
}
