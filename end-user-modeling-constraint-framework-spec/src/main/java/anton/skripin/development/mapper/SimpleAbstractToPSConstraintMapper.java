package anton.skripin.development.mapper;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.instance.InstanceElement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Simple implementation of {@link AbstractToPSConstraintMapper}.
 * Unless necessary, it should not be used in production
 */
public class SimpleAbstractToPSConstraintMapper implements AbstractToPSConstraintMapper<String> {

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

    /**
     * Maps a list of {@link InstanceElement} to its string representation in JSON-format.
     * @param subgraphForValidation subgraph required for validation
     * @return list of {@link InstanceElement} in a JSON representation
     */
    @Override
    public String mapToPlatformSpecificGraph(List<InstanceElement> subgraphForValidation) {
        try {
            return new ObjectMapper().writeValueAsString(subgraphForValidation);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error occurred while mapping an abstract to platform-specific graph");
        }
    }
}
