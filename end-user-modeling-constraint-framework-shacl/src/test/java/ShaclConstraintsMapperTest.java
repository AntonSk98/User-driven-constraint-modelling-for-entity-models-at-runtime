import ansk.development.domain.ShaclConstraintData;
import ansk.development.domain.ShaclConstraintShape;
import ansk.development.mapper.ShaclConstraintMapper;
import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.exception.constraint.GraphConstraintException;
import anton.skripin.development.exception.constraint.function.FunctionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Test cases to check abstract to SHACL-specific constraint transformation and their validation.
 */
public class ShaclConstraintsMapperTest {

    private ShaclConstraintMapper constraintMapper = new ShaclConstraintMapper();

    private ShaclConstraintData shaclConstraintData;

    private static final String JOHN_UUID = "ea9f52ee-a86f-48f1-b9c3-b259764a6b04";

    @BeforeEach
    public void init() {
        List<InstanceElement> instances = TestGraphProvider.getSubgraph();
        shaclConstraintData = this.constraintMapper.mapToPlatformSpecificGraph(instances);
    }


    @Test
    public void and() {
        final String constraintName = "and";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void equals() {
        final String constraintName = "equals";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void forAll() {
        final String constraintName = "for_all";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        // Since forAll() function cannot be implemented using SHACL mechanisms, this function behaves as forSome()...
        // For this reason an invalid constraint is expected to return true.
        // For more information see forAll() function definition.
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void forExactly() {
        final String constraintName = "for_exactly";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void forNone() {
        final String constraintName = "for_none";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void forSome() {
        final String constraintName = "for_some";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void greaterThan() {
        final String constraintName = "greater_than";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void greaterThanOrEquals() {
        final String constraintName = "greater_than_or_equals";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void lessThan() {
        final String constraintName = "less_than";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void lessThanOrEquals() {
        final String constraintName = "less_than_or_equals";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void maxLength() {
        final String constraintName = "max_length";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void minLength() {
        final String constraintName = "min_length";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void notNullEmpty() {
        final String constraintName = "not_null_or_empty";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
    }

    @Test
    public void unique() {
        final String constraintName = "unique";
        Assertions.assertThrows(GraphConstraintException.class, () -> getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
    }

    @Test
    public void minCardinality() {
        final String constraintName = "min_cardinality";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void maxCardinality() {
        final String constraintName = "max_cardinality";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void or() {
        final String constraintName = "or";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void complexConstraint() {
        final String constraintName = "complex_inner";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void nestedWithLambdaConstraint() {
        final String constraintName = "nested_logical_with_for_all";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void combiningAllComplex() {
        final String constraintName = "combining_all_complex";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }

    @Test
    public void forAllLambdaAsLogical() {
        final String constraintName = "for_all_lambda_as_logical";
        Assertions.assertThrows(FunctionException.class,
                () -> getConstraintEvaluationResult(fetchConstraint(constraintName, false)),
                "Logical functions cannot be used as lambda functions!");
    }

    @Test
    public void manyNestedLogicalConstraints() {
        final String constraintName = "many_nested_logicals";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
        final String constraintNameTwo = "many_nested_logicals_two";
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintNameTwo, false)));
    }

    @Test
    public void runtimeConstraint() {
        final String constraintName = "runtime_constraint";
        Assertions.assertTrue(getConstraintEvaluationResult(fetchConstraint(constraintName, true)));
        Assertions.assertFalse(getConstraintEvaluationResult(fetchConstraint(constraintName, false)));
    }


    private Constraint fetchConstraint(String constraintName, boolean valid) {
        try {
            String path = String.format("src/test/resources/constraints/%s/%s.json", valid ? "valid" : "invalid", constraintName);
            String constraintJson = new String(Files.readAllBytes(Path.of(path)));
            return new ObjectMapper().readValue(constraintJson, Constraint.class);
        } catch (IOException e) {
            throw new FunctionException("Failed to fetch a constraint from file resources: " + constraintName);
        }
    }

    private Boolean getConstraintEvaluationResult(Constraint constraint) {
        ShaclConstraintShape constraintShape = constraintMapper.mapToPlatformSpecificConstraint(JOHN_UUID, constraint);
        Shapes shapes = Shapes.parse(constraintShape.getGraph());
        ValidationReport report = ShaclValidator.get().validate(shapes, shaclConstraintData.getGraph());

        RDFDataMgr.write(System.out, shaclConstraintData, Lang.TTL);
        System.out.println("***************************************");
        RDFDataMgr.write(System.out, constraintShape.getGraph(), Lang.TTL);
        
        return report.conforms();
    }
}