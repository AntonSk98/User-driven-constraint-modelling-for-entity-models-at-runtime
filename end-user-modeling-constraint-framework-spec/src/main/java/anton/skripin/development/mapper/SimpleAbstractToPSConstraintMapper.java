package anton.skripin.development.mapper;

import anton.skripin.development.domain.constraint.Constraint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Simple implementation of {@link AbstractToPSConstraintMapper}.
 * Unless necessary, it should not be used in production
 */
public class SimpleAbstractToPSConstraintMapper implements AbstractToPSConstraintMapper {

    /**
     * Maps a {@link Constraint} to its string representation in JSON-format.
     *
     * @param constraint {@link Constraint}
     * @return {@link Constraint} in a JSON representation
     */
    @Override
    public String mapToPlatformSpecificConstraint(Constraint constraint) {
        try {
            return new ObjectMapper().writeValueAsString(constraint);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error occurred while mapping abstract to platform-specific constraint!");
        }
    }
}
