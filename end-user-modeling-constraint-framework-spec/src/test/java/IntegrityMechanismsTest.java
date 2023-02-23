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

import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.functions.ConstraintFunction;
import ansk.development.domain.constraint.functions.types.CollectionBasedFunction;
import ansk.development.domain.constraint.functions.types.LogicalFunction;
import ansk.development.domain.constraint.functions.types.RangeBasedFunction;
import ansk.development.domain.constraint.functions.types.StringBasedFunction;
import ansk.development.domain.integrity.IntegrityReport;
import ansk.development.service.ConstraintIntegrityServiceImpl;
import ansk.development.service.SimpleConstraintPersistenceService;
import ansk.development.service.api.ConstraintIntegrityService;
import ansk.development.service.api.ConstraintPersistenceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ansk.development.domain.constraint.functions.FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES;
import static ansk.development.domain.constraint.functions.FunctionMetadata.FunctionNames.*;

/**
 * Showcases model element integrity mechanisms.
 */
public class IntegrityMechanismsTest {

    ConstraintPersistenceService constraintPersistenceService;
    ConstraintIntegrityService constraintIntegrityService;

    @BeforeEach
    public void init() {
        this.constraintPersistenceService = new SimpleConstraintPersistenceService();
        this.constraintIntegrityService = new ConstraintIntegrityServiceImpl();
    }

    /**
     * Shows the workflow to keep constraints valid in the presence of model element changes.
     * Here, the definition of a model element changes and not its content.
     */
    @Test
    public void checkModelElementBackwardLinks() {
        final String softwareEngineerModelElement = "SoftwareEngineer";
        final String projectModelElement = "Project";
        final String sprintModelElement = "Sprint";

        Constraint testConstraint = getTestConstraint();

        constraintPersistenceService.saveConstraint(testConstraint.getModelElementType(), testConstraint);
        updateAttribute(softwareEngineerModelElement, "age", "year");
        Assertions.assertTrue(
                testConstraint
                        .getConstraintFunction()
                        .booleanFunctions()
                        .get()
                        .get(1)
                        .attribute()
                        .get()
                        .contains("year")
        );

        updateAttribute(softwareEngineerModelElement, "year", "age");
        Assertions.assertTrue(
                testConstraint
                        .getConstraintFunction()
                        .booleanFunctions()
                        .get()
                        .get(1)
                        .attribute()
                        .get()
                        .contains("age")
        );
        updateAttribute(sprintModelElement, "name", "title");
        Assertions.assertTrue(
                testConstraint
                        .getConstraintFunction()
                        .booleanFunctions()
                        .get()
                        .get(2)
                        .lambdaFunction()
                        .get()
                        .attribute()
                        .get()
                        .contains("title")
        );
        updateAttribute(sprintModelElement, "title", "name");
        Assertions.assertTrue(
                testConstraint
                        .getConstraintFunction()
                        .booleanFunctions()
                        .get()
                        .get(2)
                        .lambdaFunction()
                        .get()
                        .attribute()
                        .get()
                        .contains("name")
        );
        updateAttribute(projectModelElement, "works_on", "involves");
        Assertions.assertTrue(
                testConstraint
                        .getConstraintFunction()
                        .booleanFunctions()
                        .get()
                        .get(2)
                        .navigation()
                        .get()
                        .contains("involves")
        );
        updateAttribute(projectModelElement, "involves", "works_on");
        Assertions.assertTrue(
                testConstraint
                        .getConstraintFunction()
                        .booleanFunctions()
                        .get()
                        .get(2)
                        .navigation()
                        .get()
                        .contains("works_on")
        );
        Assertions.assertEquals(1, constraintPersistenceService.getAllConstraints(softwareEngineerModelElement).size());
        Assertions.assertEquals(1, constraintPersistenceService.getAllBackwardLinkConstraints(projectModelElement).size());
        Assertions.assertEquals(1, constraintPersistenceService.getAllBackwardLinkConstraints(sprintModelElement).size());
        constraintPersistenceService.removeConstraintByUuid(testConstraint.getUuid());
        Assertions.assertEquals(0, constraintPersistenceService.getAllConstraints(softwareEngineerModelElement).size());
        Assertions.assertEquals(0, constraintPersistenceService.getAllBackwardLinkConstraints(projectModelElement).size());
        Assertions.assertEquals(0, constraintPersistenceService.getAllBackwardLinkConstraints(sprintModelElement).size());
    }

    /**
     * Shows the framework workflow to provide integrity mechanisms upon deleting a property of a model element.
     */
    @Test
    public void testIntegrityMechanismsUponModelPropertyDeletion() {
        Constraint testConstraint = getTestConstraint();
        constraintPersistenceService.saveConstraint("SoftwareEngineer", testConstraint);
        IntegrityReport integrityReportRemovedDirectAttributeName = removeAttribute("SoftwareEngineer", "name");
        Assertions.assertEquals(1, integrityReportRemovedDirectAttributeName.getDeletedConstraints().size());
        IntegrityReport integrityReportRemovedDirectAttributeAge = removeAttribute("SoftwareEngineer", "age");
        Assertions.assertEquals(1, integrityReportRemovedDirectAttributeAge.getDeletedConstraints().size());
        IntegrityReport integrityReportRemovedAssociation = removeAttribute("Project", "consists_of");
        Assertions.assertEquals(1, integrityReportRemovedAssociation.getDeletedConstraints().size());
        IntegrityReport integrityReportRemovedAttributeViaNavigation = removeAttribute("Sprint", "name");
        Assertions.assertEquals(1, integrityReportRemovedAttributeViaNavigation.getDeletedConstraints().size());
    }

    /**
     * Function to mock the workflow of updating a property on a model element.
     *
     * @param modelElement    model element
     * @param oldPropertyName property name
     * @param newPropertyName new property name
     * @return {@link IntegrityReport}
     */
    private IntegrityReport updateAttribute(String modelElement, String oldPropertyName, String newPropertyName) {
        List<Constraint> directConstraints = constraintPersistenceService.getAllConstraints(modelElement);
        List<Constraint> backwardLinkConstraints = constraintPersistenceService.getAllBackwardLinkConstraints(modelElement);
        List<Constraint> merged = Stream.concat(directConstraints.stream(), backwardLinkConstraints.stream()).distinct().collect(Collectors.toList());
        return constraintIntegrityService.synchronizeConstraints(modelElement, oldPropertyName, newPropertyName, merged);
    }

    /**
     * Function to mock the workflow of deleting a property on a model element.
     *
     * @param modelElement        model element
     * @param removedPropertyName removed property name
     * @return {@link IntegrityReport}
     */
    private IntegrityReport removeAttribute(String modelElement, String removedPropertyName) {
        List<Constraint> directConstraints = constraintPersistenceService.getAllConstraints(modelElement);
        List<Constraint> backwardLinkConstraints = constraintPersistenceService.getAllBackwardLinkConstraints(modelElement);
        List<Constraint> merged = Stream.concat(directConstraints.stream(), backwardLinkConstraints.stream()).distinct().collect(Collectors.toList());
        return constraintIntegrityService.getInvalidConstraints(modelElement, removedPropertyName, merged);
    }

    private Constraint getTestConstraint() {
        ConstraintFunction lessThan = new RangeBasedFunction(
                LESS_THAN,
                "<SoftwareEngineer>name",
                Map.of(FUNCTION_TO_PARAMETER_NAMES.get(LESS_THAN).get(0), "65")
        );

        ConstraintFunction greaterThan = new RangeBasedFunction(
                GREATER_THAN,
                "<SoftwareEngineer>age",
                Map.of(FUNCTION_TO_PARAMETER_NAMES.get(GREATER_THAN).get(0), "18"));

        ConstraintFunction minLength = new StringBasedFunction(
                MIN_LENGTH,
                "<Sprint>name",
                Map.of(FUNCTION_TO_PARAMETER_NAMES.get(MIN_LENGTH).get(0), "5")
        );

        ConstraintFunction forAll = new CollectionBasedFunction(
                FOR_ALL,
                "works_on(Project).consists_of(Sprint)",
                minLength,
                Collections.emptyMap()
        );

        ConstraintFunction and = new LogicalFunction(AND, List.of(lessThan, greaterThan, forAll));

        Constraint testConstraint = new Constraint();
        testConstraint.setUuid("1");
        testConstraint.setModelElementType("SoftwareEngineer");
        testConstraint.setConstraintFunction(and);
        return testConstraint;
    }
}
