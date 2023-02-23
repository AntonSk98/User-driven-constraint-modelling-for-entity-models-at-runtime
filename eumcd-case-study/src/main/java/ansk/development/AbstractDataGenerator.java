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

package ansk.development;

import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.ViolationLevel;
import ansk.development.domain.constraint.functions.ConstraintFunction;
import ansk.development.domain.constraint.functions.types.CollectionBasedFunction;
import ansk.development.domain.constraint.functions.types.LogicalFunction;
import ansk.development.domain.constraint.functions.types.RangeBasedFunction;
import ansk.development.domain.constraint.functions.types.StringBasedFunction;
import ansk.development.domain.instance.InstanceElement;
import ansk.development.domain.instance.Link;
import ansk.development.domain.instance.Slot;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static ansk.development.domain.constraint.functions.FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES;
import static ansk.development.domain.constraint.functions.FunctionMetadata.FunctionNames.*;

/**
 * Static class to generate test data for experiments.
 */
public class AbstractDataGenerator {

    public final static String SOFTWARE_ENGINEER_TYPE = "SoftwareEngineer";
    public final static String PROJECT_TYPE = "Project";
    public final static String SPRINT_TYPE = "Sprint";

    /**
     * Generates a sample graph containing instances of three model types.
     *
     * @param totalNumber total number of instances that should be generated
     * @return list of {@link InstanceElement}
     */
    public static List<InstanceElement> generateDataForGraphTransformation(double totalNumber) {
        long softwareEngineerTotalNumber = Math.round(totalNumber * 0.1);
        long projectTotalNumber = Math.round(totalNumber * 0.2);
        long sprintTotalNumbers = Math.round(totalNumber * 0.7);

        List<InstanceElement> graph = new ArrayList<>();

        graph.addAll(generateSoftwareEngineers(softwareEngineerTotalNumber));

        graph.addAll(generateProjects(projectTotalNumber));

        graph.addAll(generateSprints(sprintTotalNumbers));

        List<InstanceElement> softwareEngineers = graph
                .stream()
                .filter(instanceElement -> instanceElement.getInstanceOf().equals(SOFTWARE_ENGINEER_TYPE))
                .collect(Collectors.toList());

        List<InstanceElement> projects = graph
                .stream()
                .filter(instanceElement -> instanceElement.getInstanceOf().equals(PROJECT_TYPE))
                .collect(Collectors.toList());

        List<InstanceElement> sprints = graph
                .stream()
                .filter(instanceElement -> instanceElement.getInstanceOf().equals(SPRINT_TYPE))
                .collect(Collectors.toList());

        softwareEngineers.forEach(softwareEngineer -> linkWithRandomInstances(softwareEngineer, projects, "works_on", 1));
        projects.forEach(project -> linkWithRandomInstances(project, sprints, "consists_of", 1));

        return graph;
    }

    /**
     * Combines a list of constraint functions into one constraint function united by AND operator.
     *
     * @param collectionBasedFunctions list of constraint functions
     * @return {@link Constraint}
     */
    public static Constraint combineConstraintFunctionsIntoOneConstraint(List<ConstraintFunction> collectionBasedFunctions) {
        ConstraintFunction and = new LogicalFunction(AND, collectionBasedFunctions);
        return generateConstraint(and);
    }

    /**
     * Generate a list of collection-based functions.
     *
     * @param totalNumber total number of functions to be generated
     * @return list of collection-based functions
     */
    public static List<ConstraintFunction> collectionBasedFunctions(double totalNumber) {
        final String navigation = String.format("works_on(%s)", PROJECT_TYPE);
        final String attribute = String.format("<%s>name", PROJECT_TYPE);
        final String value = String.valueOf(ThreadLocalRandom.current().nextInt(5, 20));
        List<ConstraintFunction> constraintFunctions = new ArrayList<>();
        for (int i = 0; i < totalNumber; i++) {
            if (i % 2 == 0) {
                constraintFunctions.add(forSome(navigation, attribute, value));
            } else {
                constraintFunctions.add(forNone(navigation, attribute, value));
            }
        }
        return constraintFunctions;
    }

    /**
     * Generates a list of attribute-based functions.
     *
     * @param totalNumber total number of functions to be generated
     * @return list of attribute-based functions
     */
    public static List<ConstraintFunction> attributeBasedFunctions(double totalNumber) {
        List<ConstraintFunction> constraintFunctions = new ArrayList<>();
        for (int i = 0; i < totalNumber; i++) {
            if (i % 2 == 0) {
                constraintFunctions.add(greaterThan(String.format("<%s>age", SOFTWARE_ENGINEER_TYPE), String.valueOf(ThreadLocalRandom.current().nextInt(16, 65))));
            } else {
                constraintFunctions.add(minLength(String.format("<%s>name", SOFTWARE_ENGINEER_TYPE), String.valueOf(ThreadLocalRandom.current().nextInt(1, 15))));
            }
        }
        return constraintFunctions;
    }

    /**
     * Retrieves a random instance uuid of a given type from a provided graph.
     *
     * @param graph graph
     * @param type  type
     * @return random instance uuid
     */
    public static String getRandomUuidByType(List<InstanceElement> graph, String type) {
        List<String> elementByTypeUuids = graph
                .stream()
                .filter(instanceElement -> instanceElement.getInstanceOf().equals(type))
                .map(InstanceElement::getUuid)
                .collect(Collectors.toList());
        if (elementByTypeUuids.size() == 0) {
            return graph.get(ThreadLocalRandom.current().nextInt(graph.size())).getUuid();
        }
        return elementByTypeUuids.get(ThreadLocalRandom.current().nextInt(elementByTypeUuids.size()));
    }

    /**
     * Generates interval where each next step is multiplied by a given multiplier.
     *
     * @param steps      number of steps
     * @param multiplier provided multiplier
     * @return list of intervals
     */
    public static List<Double> generateIntervalsByMultiplying(int steps, int multiplier) {
        List<Double> intervals = new ArrayList<>();
        intervals.add(1.0);
        for (int i = 1; i <= steps; i++) {
            intervals.add((double) (i * multiplier));
        }
        return intervals;
    }

    /**
     * Generates interval where a value is increased by addition with a given addition value.
     *
     * @param steps    number of steps
     * @param addition provided addition
     * @return list of intervals
     */
    public static List<Double> generateIntervalsByAddition(int steps, int addition) {
        List<Double> intervals = new ArrayList<>();
        intervals.add(1.0);
        double initial = 0.0;
        for (int i = 1; i <= steps; i++) {
            initial += addition;
            intervals.add(initial);
        }
        return intervals;
    }

    private static List<InstanceElement> generateSoftwareEngineers(long totalNumber) {
        List<InstanceElement> softwareEngineers = new ArrayList<>();
        for (int i = 0; i < totalNumber; i++) {
            softwareEngineers.add(createSoftwareEngineer(
                    RandomStringUtils.randomAlphabetic(ThreadLocalRandom.current().nextInt(5, 11)),
                    String.valueOf(ThreadLocalRandom.current().nextInt(5000, 11000)),
                    String.valueOf(ThreadLocalRandom.current().nextInt(16, 70))
            ));
        }
        return softwareEngineers;
    }

    private static List<InstanceElement> generateProjects(long totalNumber) {
        List<InstanceElement> projects = new ArrayList<>();
        for (int i = 0; i < totalNumber; i++) {
            projects.add(createProjectInstance(
                    RandomStringUtils.randomAlphabetic(ThreadLocalRandom.current().nextInt(7, 15)),
                    new Random().nextBoolean(),
                    RandomStringUtils.randomAlphabetic(ThreadLocalRandom.current().nextInt(5, 11))
            ));
        }
        return projects;
    }

    private static List<InstanceElement> generateSprints(long totalNumber) {
        List<InstanceElement> sprints = new ArrayList<>();
        for (int i = 0; i < totalNumber; i++) {
            sprints.add(createSprintInstance(RandomStringUtils.randomAlphabetic(ThreadLocalRandom.current().nextInt(5, 11))));
        }
        return sprints;
    }

    private static ConstraintFunction forSome(String navigation, String attribute, String value) {
        return new CollectionBasedFunction(
                FOR_SOME,
                navigation,
                minLength(attribute, value),
                null
        );
    }

    private static ConstraintFunction forNone(String navigation, String attribute, String value) {
        return new CollectionBasedFunction(
                FOR_NONE,
                navigation,
                greaterThan(attribute, value),
                null
        );
    }

    private static ConstraintFunction minLength(String attribute, String value) {
        return new StringBasedFunction(
                MIN_LENGTH,
                attribute,
                Map.of(FUNCTION_TO_PARAMETER_NAMES.get(MIN_LENGTH).get(0), value)
        );
    }

    private static ConstraintFunction greaterThan(String attribute, String value) {
        return new RangeBasedFunction(
                GREATER_THAN,
                attribute,
                Map.of(FUNCTION_TO_PARAMETER_NAMES.get(GREATER_THAN).get(0), value));
    }

    private static Constraint generateConstraint(ConstraintFunction constraintFunction) {
        Constraint constraint = new Constraint();
        constraint.setUuid(UUID.randomUUID().toString());
        constraint.setViolationLevel(ViolationLevel.ERROR);
        constraint.setConstraintFunction(constraintFunction);
        return constraint;
    }


    private static InstanceElement createSoftwareEngineer(String name, String salary, String age) {
        InstanceElement instanceElement = new InstanceElement();
        instanceElement.setUuid(UUID.randomUUID().toString());
        instanceElement.setInstanceOf(SOFTWARE_ENGINEER_TYPE);
        Slot nameSlot = new Slot();
        nameSlot.setKey("name");
        nameSlot.setValue(name);
        Slot salarySlot = new Slot();
        salarySlot.setKey("salary");
        salarySlot.setValue(salary);
        Slot ageSlot = new Slot();
        ageSlot.setKey("age");
        ageSlot.setValue(age);
        instanceElement.setSlots(List.of(nameSlot, salarySlot, ageSlot));
        return instanceElement;
    }

    private static InstanceElement createProjectInstance(String name, boolean started, String responsible) {
        InstanceElement instanceElement = new InstanceElement();
        instanceElement.setUuid(UUID.randomUUID().toString());
        instanceElement.setInstanceOf(PROJECT_TYPE);
        Slot nameSlot = new Slot();
        nameSlot.setKey("name");
        nameSlot.setValue(name);
        Slot startedSlot = new Slot();
        startedSlot.setKey("started");
        startedSlot.setValue(String.valueOf(started));
        Slot responsibleSlot = new Slot();
        responsibleSlot.setKey("responsible");
        responsibleSlot.setValue(responsible);
        instanceElement.setSlots(List.of(nameSlot, startedSlot, responsibleSlot));
        return instanceElement;
    }

    private static InstanceElement createSprintInstance(String name) {
        InstanceElement instanceElement = new InstanceElement();
        instanceElement.setUuid(UUID.randomUUID().toString());
        instanceElement.setInstanceOf(SPRINT_TYPE);
        Slot nameSlot = new Slot();
        nameSlot.setKey("name");
        nameSlot.setValue(name);
        instanceElement.setSlots(List.of(nameSlot));
        return instanceElement;
    }

    private static void linkWithRandomInstances(InstanceElement instanceElement, List<InstanceElement> scope, String byRelation, int linkingNumber) {
        int scopeSize = scope.size();
        while (linkingNumber > 0) {
            var randomInstance = scope.get(ThreadLocalRandom.current().nextInt(0, scopeSize));
            if (instanceElement.getLinks() == null || instanceElement.getLinks().stream().noneMatch(link -> link.getTargetInstanceUuid().equals(randomInstance.getUuid()))) {
                linkTwoInstances(instanceElement, randomInstance, byRelation);
                linkingNumber--;
            }
        }
    }

    private static void linkTwoInstances(InstanceElement context, InstanceElement target, String byRelation) {
        if (context.getLinks() == null) {
            context.setLinks(new ArrayList<>());
        }
        Link link = new Link();
        link.setInstanceUuid(context.getUuid());
        link.setTargetInstanceUuid(target.getUuid());
        link.setName(byRelation);
        context.getLinks().add(link);
    }
}
