import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import anton.skripin.development.domain.constraint.functions.types.LogicalFunction;
import anton.skripin.development.domain.constraint.functions.types.StringBasedFunction;
import anton.skripin.development.service.AbstractConstraintValidationService;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationServiceTest {

    @Test
    @Description("Should return a set of 2 lists each containing required graph types for further constraint validation")
    public void testGetRequiredSubgraphElements() {
        AbstractConstraintValidationService abstractConstraintValidationService = new AbstractConstraintValidationService();

        ConstraintFunction minLength = new StringBasedFunction("minLength", "#(SoftwareEngineer)", Collections.emptyMap());
        ConstraintFunction forAll = new StringBasedFunction("forAll", "#(SoftwareEngineer).works_on(Project).consists_of(Sprint)", Collections.emptyMap());
        ConstraintFunction and = new LogicalFunction("and", List.of(minLength, forAll));
        ConstraintFunction maxLength = new StringBasedFunction("maxLength", "#(SoftwareEngineer)", Collections.emptyMap());
        ConstraintFunction or = new LogicalFunction("or", List.of(maxLength, and));

        Constraint constraint = new Constraint();
        constraint.setConstraintFunction(or);

        Set<List<String>> subgraphTypesSet = abstractConstraintValidationService.getRequiredSubgraphElements(constraint);
        assertEquals(2, subgraphTypesSet.size());
        assertEquals(1, subgraphTypesSet
                .stream()
                .filter(list -> !list.contains("Project"))
                .toList()
                .stream()
                .findFirst()
                .orElseThrow()
                .size());
        assertEquals(3, subgraphTypesSet
                .stream()
                .filter(list -> list.contains("Project"))
                .toList()
                .stream()
                .findFirst()
                .orElseThrow()
                .size());
        var underTest = subgraphTypesSet
                .stream()
                .filter(list -> list.contains("Project"))
                .toList()
                .stream()
                .findFirst()
                .orElseThrow();
        assertTrue(underTest.contains("SoftwareEngineer"));
        assertTrue(underTest.contains("Project"));
        assertTrue(underTest.contains("Sprint"));
    }
}
