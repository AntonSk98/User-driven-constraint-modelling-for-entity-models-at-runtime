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
import ansk.development.domain.constraint.functions.types.StringBasedFunction;
import ansk.development.exception.constraint.function.FunctionValidationException;
import ansk.development.service.AbstractConstraintValidationService;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ansk.development.domain.ValidationUtils.validateAttribute;
import static ansk.development.domain.ValidationUtils.validateNavigation;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationServiceTest {

    @Test
    @Description("Should return a set of 2 lists each containing required graph types for further constraint validation")
    public void testGetRequiredSubgraphElements() {
        AbstractConstraintValidationService abstractConstraintValidationService = new AbstractConstraintValidationService();

        ConstraintFunction minLength = new StringBasedFunction("minLength",
                "<SoftwareEngineer>name",
                Collections.emptyMap());

        ConstraintFunction forAll = new CollectionBasedFunction("forAll",
                "works_on(Project).consists_of(Sprint)",
                new StringBasedFunction("minLength",
                        "<Sprint>name",
                        Collections.emptyMap()), Collections.emptyMap());

        ConstraintFunction and = new LogicalFunction("and",
                List.of(minLength, forAll));

        ConstraintFunction maxLength = new StringBasedFunction("maxLength",
                "<SoftwareEngineer>age",
                Collections.emptyMap());
        ConstraintFunction or = new LogicalFunction("or",
                List.of(maxLength, and));

        Constraint constraint = new Constraint();
        constraint.setModelElementType("SoftwareEngineer");
        constraint.setConstraintFunction(or);

        Set<Set<String>> subgraphTypesSet = abstractConstraintValidationService.getRequiredSubgraphElements(constraint);
        assertEquals(2, subgraphTypesSet.size());
        assertEquals(1, subgraphTypesSet
                .stream()
                .filter(list -> !list.contains("Project"))
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElseThrow()
                .size());
        assertEquals(3, subgraphTypesSet
                .stream()
                .filter(list -> list.contains("Project"))
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElseThrow()
                .size());
        var underTest = subgraphTypesSet
                .stream()
                .filter(list -> list.contains("Project"))
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElseThrow();
        assertTrue(underTest.contains("SoftwareEngineer"));
        assertTrue(underTest.contains("Project"));
        assertTrue(underTest.contains("Sprint"));
    }

    @Test
    @Description("Checks the correctness of the navigation validation util")
    public void testNavigationValidation() {
        var validNavigation1 = "takes_part_in(Sprint)";
        var validNavigation2 = "takes_part_in(Sprint).participates(SoftwareEngineer)";
        var validNavigation3 = "takes_part_in(Sprint).participates(SoftwareEngineer).works_on(Project)";

        var invalidNavigation1 = ".takes_part_in(Sprint)";
        var invalidNavigation2 = "takes_part_in(Sprint).";
        var invalidNavigation3 = "<SoftwareEngineer>name";

        List<String> validNavigations = List.of(validNavigation1, validNavigation2, validNavigation3);
        List<String> invalidNavigations = List.of(invalidNavigation1, invalidNavigation2, invalidNavigation3);

        validNavigations.forEach(navigation -> assertDoesNotThrow(() -> validateNavigation(navigation)));
        invalidNavigations.forEach(navigation -> assertThrows(FunctionValidationException.class, () -> validateNavigation(navigation)));
    }

    @Test
    @Description("Checks the correctness of the attribute validation util")
    public void testAttributeValidation() {
        var validAttribute = "<SoftwareEngineer>name";
        var invalidAttribute1 = ".<SoftwareEngineer>name";
        var invalidAttribute2 = "<SoftwareEngineer>name.";

        assertDoesNotThrow(() -> validateAttribute(validAttribute));
        assertThrows(FunctionValidationException.class, () -> validateAttribute(invalidAttribute1));
        assertThrows(FunctionValidationException.class, () -> validateAttribute(invalidAttribute2));
    }
}
