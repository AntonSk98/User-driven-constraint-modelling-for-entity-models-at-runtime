/*
 * Copyright (c) 2023 Anton Skripin
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

import ansk.development.dsl.ShaclInstanceGraph;
import ansk.development.dsl.ShaclConstraintShape;
import ansk.development.domain.instance.InstanceElement;
import ansk.development.exception.constraint.GraphConstraintException;
import ansk.development.mapper.ShaclConstraintMapper;
import jdk.jfr.Description;
import org.apache.jena.graph.Graph;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Tests SHACL constraints and evaluates them.
 */
public class ShaclConstraintTest {

    private static final String JOHN_UUID = "ea9f52ee-a86f-48f1-b9c3-b259764a6b04";
    private static final String PROJECT_UUID = "24620947-6e86-4f39-8365-40247265c9ce";

    private static final String SPRINT_UUID = "babd56ed-6b5d-4a0a-a364-0039777f098c";

    private ShaclInstanceGraph dataGraph;

    @BeforeEach
    public void init() {
        List<InstanceElement> testGraph = TestGraphProvider.getSubgraph();
        dataGraph = new ShaclConstraintMapper().mapToPlatformSpecificGraph(testGraph);
    }

    @Test
    @Description("The length's name of all sprints of all project a Software Engineer John is working on is at least 14")
    public void forAll() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(JOHN_UUID);
        List<String> navigation = List.of("works_on", "consists_of");
        constraint.forAll(navigation,
                constraint.minLength("name", "14", true),
                dataGraph.getNumberOfConnectedLinks(JOHN_UUID, navigation),
                false);
        Assertions.assertTrue(this.validateConstraint(constraint.getGraph()));


        ShaclConstraintShape invalidConstraint = new ShaclConstraintShape();
        invalidConstraint.loadTargetInstance(JOHN_UUID);
        invalidConstraint.forAll(navigation,
                invalidConstraint.minLength("name", "20", true),
                dataGraph.getNumberOfConnectedLinks(JOHN_UUID, navigation),
                false);
        Assertions.assertFalse(this.validateConstraint(invalidConstraint.getGraph()));
    }

    @Test
    @Description("The length's name of at least one sprint among all projects a Software Engineer John is working is more than 20")
    public void forSome() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(JOHN_UUID);
        constraint.forSome(List.of("works_on", "consists_of"), constraint.minLength("name", "20", true), false);
        Assertions.assertTrue(this.validateConstraint(constraint.getGraph()));
    }

    @Test
    @Description("The length's name of all tickets must not exceed 255 characters")
    public void forNone() {
        ShaclConstraintShape trueConstraint = new ShaclConstraintShape();
        trueConstraint.loadTargetInstance(JOHN_UUID);
        trueConstraint.forNone(List.of("works_on", "consists_of"), trueConstraint.minLength("name", "255", true), false);
        Assertions.assertTrue(this.validateConstraint(trueConstraint.getGraph()));
        ShaclConstraintShape falseConstraint = new ShaclConstraintShape();
        falseConstraint.loadTargetInstance(JOHN_UUID);
        falseConstraint.forSome(List.of("works_on", "consists_of"), falseConstraint.minLength("name", "255", true), false);
        Assertions.assertFalse(this.validateConstraint(falseConstraint.getGraph()));
    }

    @Test
    @Description("Checks whether a Software Engineer John has a name containing at least 1 characters")
    public void minLength() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(JOHN_UUID);
        constraint.minLength("name", "1", false);
        Assertions.assertTrue(this.validateConstraint(constraint.getGraph()));
    }

    @Test
    @Description("Checks whether a Software Engineer John has a name not exceeding 255 characters")
    public void maxLength() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(JOHN_UUID);
        constraint.maxLength("name", "255", false);
        Assertions.assertTrue(this.validateConstraint(constraint.getGraph()));
    }

    @Test
    @Description("John does not earn more than 10000")
    public void greaterThan() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(JOHN_UUID);
        constraint.greaterThan("salary", "10000", false);
        Assertions.assertFalse(this.validateConstraint(constraint.getGraph()));
    }

    @Test
    @Description("John is an adult")
    public void greaterThanOrEquals() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(JOHN_UUID);
        constraint.greaterThanOrEquals("age", "18", false);
        Assertions.assertTrue(this.validateConstraint(constraint.getGraph()));
    }

    @Test
    @Description("John earns less than 11000")
    public void lessThan() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(JOHN_UUID);
        constraint.lessThan("salary", "11000", false);
        Assertions.assertTrue(this.validateConstraint(constraint.getGraph()));
    }

    @Test
    @Description("John earns at least 10000")
    public void lessThanOrEquals() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(JOHN_UUID);
        constraint.lessThanOrEquals("salary", "10000", false);
        Assertions.assertTrue(this.validateConstraint(constraint.getGraph()));
    }

    @Test
    @Description("Age of John is 24")
    public void equals() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(JOHN_UUID);
        constraint.equals("age", "24", false);
        Assertions.assertTrue(this.validateConstraint(constraint.getGraph()));
    }

    @Test
    @Description("John is adult and he is responsible himself for all projects he works on")
    public void and() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(JOHN_UUID);
        List<String> navigation = List.of("works_on");
        Resource isAdult = constraint.greaterThanOrEquals("age", "18", true);
        Resource responsibleForAllProjects = constraint.forAll(navigation,
                constraint.equals("responsible", "John", true),
                dataGraph.getNumberOfConnectedLinks(JOHN_UUID, navigation),
                true);
        constraint.and(List.of(isAdult, responsibleForAllProjects), false);
        Assertions.assertTrue(this.validateConstraint(constraint.getGraph()));
    }

    @Test
    @Description("Every project must have only one sprint named 'Design process'")
    public void forExactly() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(PROJECT_UUID);
        constraint.forExactly(List.of("consists_of"),
                constraint.equals("name", "Design process", true),
                1,
                false);
        Assertions.assertTrue(validateConstraint(constraint.getGraph()));
    }

    @Test
    @Description("Name of a Sprint must be unique")
    public void unique() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(SPRINT_UUID);
        Assertions.assertThrows(GraphConstraintException.class, () -> constraint.unique("name"));
    }

    @Test
    @Description("Salary of all Software Engineers working on a project must be present")
    public void notNullOrEmpty() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(PROJECT_UUID);
        constraint.forAll(List.of("participates"),
                constraint.notNullOrEmpty("salary", true),
                dataGraph.getNumberOfConnectedLinks(PROJECT_UUID, List.of("participates")),
                false);
        Assertions.assertTrue(this.validateConstraint(constraint.getGraph()));
    }

    @Test
    @Description("Software Engineer must work at least on one project")
    public void minCardinality() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(JOHN_UUID);
        constraint.minCardinality("works_on", 1);
        Assertions.assertTrue(this.validateConstraint(constraint.getGraph()));
    }

    @Test
    @Description("Software Engineer can work at most on three projects")
    public void maxCardinality() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(JOHN_UUID);
        constraint.maxCardinality("works_on", 3);
        Assertions.assertTrue(this.validateConstraint(constraint.getGraph()));
    }

    @Test
    @Description("Constraint that does not really make sense but shows complex constraints in action." +
            "A software Engineer must be adult and either his name is John or all of his/her projects must be started")
    public void complexConstraint() {
        ShaclConstraintShape constraint = new ShaclConstraintShape();
        constraint.loadTargetInstance(JOHN_UUID);

        Resource hisNameIsJohn = constraint.equals("name", "John", true);
        Resource allProjectsStarted = constraint.equals("started", "true", true);
        Resource forAll = constraint.forAll(List.of("works_on"),
                allProjectsStarted,
                dataGraph.getNumberOfConnectedLinks(JOHN_UUID, List.of("works_on")),
                true);
        Resource eitherNameJohnOrAllProjectsAreStarted = constraint.or(List.of(hisNameIsJohn, forAll), true);
        Resource isJohnAdult = constraint.greaterThan("age", "18", true);
        constraint.and(List.of(isJohnAdult, eitherNameJohnOrAllProjectsAreStarted), false);
        Assertions.assertTrue(this.validateConstraint(constraint.getGraph()));
    }

    private boolean validateConstraint(Graph shapeGraph) {
        Shapes shapes = Shapes.parse(shapeGraph);
        ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph.getGraph());
        return report.conforms();
    }

    /**
     * For testing purposes to print current RDF graph and a respective constraint.
     *
     * @param constraint
     */
    private void printDataAndConstraint(ShaclConstraintShape constraint) {
        RDFDataMgr.write(System.out, dataGraph, Lang.TTL);
        RDFDataMgr.write(System.out, constraint, Lang.TTL);
    }
}
