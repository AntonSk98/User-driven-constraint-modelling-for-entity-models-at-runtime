import ansk.development.GremlinRegistry;
import ansk.development.Registry;
import ansk.development.dsl.ConstraintGraphTraversalSource;
import ansk.development.dsl.__;
import ansk.development.exception.GraphConstraintException;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

public class GremlinConstraintsTest {

    @BeforeEach
    public void init() {
        GremlinRegistry.spawnNewGraph();
        ConstraintGraphTraversalSource graphSource = GremlinRegistry.getConstraintTraversal();
        var testGraph = Registry.getSubgraph();
        testGraph.forEach(graphSource::addInstance);
        testGraph
                .stream()
                .filter(instanceElement -> Objects.nonNull(instanceElement.getLinks()))
                .flatMap(instanceElement -> instanceElement.getLinks().stream())
                .forEach(graphSource::linkTwoInstances);
    }

    @Test
    @Description("The length's name of all sprints of all project a Software Engineer John is working on is at least 14")
    public void forAll() {
        Assertions.assertTrue(GremlinRegistry.getConstraintTraversal()
                .instanceByName("John")
                .forAll(List.of("works_on", "consists_of"), __.minLength("name", "14"))
                .next());
    }

    @Test
    @Description("The length's name of at least one sprint among all projects a Software Engineer John is working is more than 20")
    public void forSome() {
        Assertions.assertTrue(GremlinRegistry.getConstraintTraversal()
                .instanceByName("John")
                .forSome(List.of("works_on", "consists_of"), __.minLength("name", "15"))
                .next());
        Assertions.assertFalse(GremlinRegistry.getConstraintTraversal()
                .instanceByName("John")
                .forAll(List.of("works_on", "consists_of"), __.minLength("name", "15"))
                .next());
    }

    @Test
    @Description("The length's name of all tickets must not exceed 255 characters")
    public void forNone() {
        Assertions.assertTrue(GremlinRegistry.getConstraintTraversal()
                .instanceByName("John")
                .forNone(List.of("works_on", "consists_of"), __.minLength("name", "255"))
                .next());
        Assertions.assertFalse(GremlinRegistry.getConstraintTraversal()
                .instanceByName("John")
                .forSome(List.of("works_on", "consists_of"), __.minLength("name", "255"))
                .next());
    }

    @Test
    @Description("Checks whether a Software Engineer John has a name containing at least 1 characters")
    public void minLength() {
        Assertions.assertTrue(GremlinRegistry.getConstraintTraversal()
                .instanceByName("John")
                .minLength("name", "1")
                .next());
    }

    @Test
    @Description("Checks whether a Software Engineer John has a name not exceeding 255 characters")
    public void maxLength() {
        Assertions.assertTrue(GremlinRegistry.getConstraintTraversal()
                .instanceByName("John")
                .maxLength("name", "255")
                .next());
    }

    @Test
    @Description("John does not earn more than 10000")
    public void greaterThan() {
        Assertions.assertFalse(GremlinRegistry.getConstraintTraversal()
                .instanceByName("John")
                .greaterThan("salary", "10000")
                .next());
    }

    @Test
    @Description("John is an adult")
    public void greaterThanOrEquals() {
        Assertions.assertTrue(GremlinRegistry.getConstraintTraversal()
                .instanceByName("John")
                .greaterThan("age", "18")
                .next());
    }

    @Test
    @Description("John earns less than 11000")
    public void lessThan() {
        Assertions.assertTrue(GremlinRegistry.getConstraintTraversal()
                .instanceByName("John")
                .lessThan("salary", "11000")
                .next());
    }

    @Test
    @Description("John earns at least 10000")
    public void lessThanOrEquals() {
        Assertions.assertTrue(GremlinRegistry.getConstraintTraversal()
                .instanceByName("John")
                .lessThanOrEquals("salary", "10000")
                .next());
    }

    @Test
    @Description("Age of John is 24")
    public void equals() {
        Assertions.assertTrue(GremlinRegistry.getConstraintTraversal()
                .instanceByName("John")
                .equals("age", "24")
                .next());
    }

    @Test
    @Description("John is adult and he is responsible himself for all projects he works on")
    public void and() {
        Assertions.assertTrue(GremlinRegistry.getConstraintTraversal()
                .instanceByName("John")
                .and(
                        List.of(
                                __.greaterThanOrEquals("age", "18"),
                                __.forAll(List.of("works_on"), __.equals("responsible", "John")))
                )
                .next());
    }

    @Test
    @Description("Every project must have only one spring named 'Design process'")
    public void forExactly() {
        Assertions.assertTrue(GremlinRegistry.getConstraintTraversal()
                .instanceByName("Thesis")
                .forExactly(List.of("consists_of"), __.equals("name", "Design process"), 1)
                .next()
        );
    }

    @Test
    @Description("Name of a Sprint must be unique")
    public void unique() {
        Assertions.assertTrue(
                GremlinRegistry
                        .getConstraintTraversal()
                        .instanceByName("Design process")
                        .unique("name")
                        .next()
        );

        Assertions.assertThrows(
                GraphConstraintException.class,
                () -> GremlinRegistry
                        .getConstraintTraversal()
                        .instanceByName("Thesis")
                        .forAll(
                                List.of("consists_of"),
                                __.unique("name")
                        )
                        .next()
        );
    }

    @Test
    @Description("Salary of all Software Engineers working on a project must be present")
    public void notNullOrEmpty() {
        Assertions.assertTrue(
                GremlinRegistry
                        .getConstraintTraversal()
                        .instanceByName("Thesis")
                        .forAll(List.of("participates"), __.notNullOrEmpty("salary"))
                        .next()
        );
    }

    @Test
    @Description("Software Engineer must work at least on one project")
    public void minCardinality() {
        Assertions.assertTrue(
                GremlinRegistry.getConstraintTraversal().instanceByName("John").minCardinality("works_on", 1).next()
        );
    }

    @Test
    @Description("Software Engineer can work at most on three projects")
    public void maxCardinality() {
        Assertions.assertTrue(
                GremlinRegistry.getConstraintTraversal().instanceByName("John").maxCardinality("works_on", 3).next()
        );
    }

    @Test
    @Description("Constraint that does not really make sense but shows complex constraints in action." +
            "A software Engineer must be adult and either his name is John or all of his/her projects must be started")
    public void complexConstraint() {
        var traversal = GremlinRegistry
                .getConstraintTraversal();
        Assertions.assertTrue(
                traversal.instance("ea9f52ee-a86f-48f1-b9c3-b259764a6b04")
                        .and(
                                List.of(__.greaterThan("age", "20"),
                                        traversal.instance("ea9f52ee-a86f-48f1-b9c3-b259764a6b04")
                                                .or(
                                                        List.of(
                                                                __.equals("name", "John"),
                                                                __.forAll(List.of("works_on"), __.equals("started", "true"))
                                                        )
                                                ))
                        )
                        .next()
        );
    }
}
