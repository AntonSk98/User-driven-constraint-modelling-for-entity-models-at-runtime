import ansk.development.GremlinRegistry;
import ansk.development.Registry;
import ansk.development.mapper.GremlinConstraintMapper;
import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.instance.InstanceElement;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GremlinConstraintMapperTest {

    private GremlinConstraintMapper constraintMapper = new GremlinConstraintMapper();

    private static final String JOHN_UUID = "ea9f52ee-a86f-48f1-b9c3-b259764a6b04";

    @BeforeEach
    public void init() {
        GremlinRegistry.spawnNewGraph();
        List<InstanceElement> instances = Registry.getSubgraph();
        this.constraintMapper.mapToPlatformSpecificGraph(instances);
    }

    @Test
    public void and() {
        Constraint constraint = fetchConstraint("and");
        Boolean evaluationResult = getConstraintEvaluationResult(constraint);
        Assertions.assertTrue(evaluationResult);
    }

    @Test
    public void equals() {
        Constraint constraint = fetchConstraint("equals");
        Boolean evaluationResult = getConstraintEvaluationResult(constraint);
        Assertions.assertTrue(evaluationResult);
    }

    private Constraint fetchConstraint(String constraintName) {
        try {
            String path = String.format("src/test/resources/constraints/%s.json", constraintName);
            String constraintJson = new String(Files.readAllBytes(Path.of(path)));
            return new ObjectMapper().readValue(constraintJson, Constraint.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch a constraint from file resources: " + constraintName);
        }
    }

    private Boolean getConstraintEvaluationResult(Constraint constraint) {
        GraphTraversal<?, Boolean> result = constraintMapper.mapToPlatformSpecificConstraint(JOHN_UUID, constraint);
        return result.next();
    }
}
