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
import ansk.development.domain.constraint.functions.types.StringBasedFunction;
import ansk.development.domain.instance.InstanceElement;
import ansk.development.domain.instance.Link;
import ansk.development.service.SimpleConstraintPersistenceService;
import ansk.development.service.api.ConstraintPersistenceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ansk.development.domain.constraint.functions.FunctionMetadata.FunctionNames.MIN_LENGTH;

public class InstanceIntegrityMechanismsTest {

    private ConstraintPersistenceService constraintPersistenceService;

    @BeforeEach
    public void init() {
        constraintPersistenceService = new SimpleConstraintPersistenceService();
    }

    /**
     * Tests the workflow of instance-related backward link.
     */
    @Test
    public void testConstraintIntegrityInstanceLevel() {
        Constraint constraint = getTestConstraint("1", "SoftwareEngineer");
        constraintPersistenceService.saveConstraint("SoftwareEngineer", constraint);

        InstanceElement engineer = createInstance("1", "SoftwareEngineer");
        List<Constraint> constraints = constraintPersistenceService.getAllConstraints("SoftwareEngineer");
        Assertions.assertFalse(constraints.isEmpty());

        InstanceElement project = createInstance("2", "Project");

        // Whenever two instance elements are linked to each other, an instance backward link must be added
        linkTwoInstances(engineer, project, "works_on");
        constraintPersistenceService.addInstanceBackwardLink(project.getUuid(), engineer.getUuid(), constraint.getUuid());
        // In this case, whenever a project instance changes, all its direct constraints as well as all instance backward links must be evaluated
        Assertions.assertEquals(1, constraintPersistenceService.getConstraintLinksByInstanceUuid(project.getUuid()).size());

        // Whenever a link between two instances is removed, a respective instance backward link must also be removed
        Assertions.assertTrue(removeLinkBetweenTwoInstances(engineer, project, "works_on"));
        Assertions.assertTrue(constraintPersistenceService.removeInstanceBackwardLink(project.getUuid()));

        // Assert that an instance backward link is removed
        Assertions.assertEquals(0, constraintPersistenceService.getConstraintLinksByInstanceUuid(project.getUuid()).size());

        // Whenever a constraint is removed, all respective instance backward links must also be removed
        linkTwoInstances(project, engineer, "participates");
        constraintPersistenceService.addInstanceBackwardLink(engineer.getUuid(), project.getUuid(), constraint.getUuid());
        constraintPersistenceService.removeConstraintByUuid(constraint.getUuid());
        constraintPersistenceService.removeInstanceBackwardLinkByConstraintUuid(constraint.getUuid());
        Assertions.assertEquals(0, constraintPersistenceService.getConstraintLinksByInstanceUuid(engineer.getUuid()).size());
    }


    private InstanceElement createInstance(String instanceUuid, String instanceOf) {
        InstanceElement instanceElement = new InstanceElement();
        instanceElement.setUuid(instanceUuid);
        instanceElement.setInstanceOf(instanceOf);
        instanceElement.setLinks(new ArrayList<>());
        return instanceElement;
    }

    private void linkTwoInstances(InstanceElement instance, InstanceElement targetInstance, String byRelation) {
        Link link = new Link();
        link.setInstanceUuid(instance.getUuid());
        link.setTargetInstanceUuid(targetInstance.getUuid());
        link.setName(byRelation);
        instance.getLinks().add(link);
    }

    private boolean removeLinkBetweenTwoInstances(InstanceElement instance, InstanceElement targetInstance, String byRelation) {
        Link linkToBeRemoved = instance
                .getLinks()
                .stream()
                .filter(link -> link.getTargetInstanceUuid().equals(targetInstance.getUuid()) && link.getName().equals(byRelation))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Link is not found"));
        return instance.getLinks().remove(linkToBeRemoved);
    }

    private Constraint getTestConstraint(String uuid, String modelElement) {
        Constraint testConstraint = new Constraint();
        testConstraint.setUuid(uuid);
        testConstraint.setModelElementType(modelElement);
        testConstraint.setConstraintFunction(new StringBasedFunction(MIN_LENGTH, "Test", Collections.emptyMap(), true));
        return testConstraint;
    }
}
