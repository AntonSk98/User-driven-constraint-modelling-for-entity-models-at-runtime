import anton.skripin.development.domain.AttributeUtils;
import anton.skripin.development.domain.NavigationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AttributeNavigationUtilsTest {

    @Test
    public void getAttributeRoot() {
        String softwareEngineerName = "<SoftwareEngineer>name";
        String sprintDuration = "<Sprint>duration";
        Assertions.assertEquals("name", AttributeUtils.getAttributeRoot(softwareEngineerName));
        Assertions.assertEquals("duration", AttributeUtils.getAttributeRoot(sprintDuration));
    }

    @Test
    public void getNavigationRoot() {
        String firstPath = "works_on(Project)";
        String secondPath = "works_on(Project).consists_of(Sprint)";
        String thirdPath = "works_on(Project).consists_of(Sprint).participates(SoftwareEngineer)";
        Assertions.assertEquals(List.of("works_on"), NavigationUtils.getNavigationRoot(firstPath));
        Assertions.assertEquals(List.of("works_on", "consists_of"), NavigationUtils.getNavigationRoot(secondPath));
        Assertions.assertEquals(List.of("works_on", "consists_of", "participates"), NavigationUtils.getNavigationRoot(thirdPath));
    }

    @Test
    public void getNavigationTypes() {
        String firstPath = "works_on(Project)";
        String secondPath = "works_on(Project).consists_of(Sprint)";
        String thirdPath = "works_on(Project).consists_of(Sprint).participates(SoftwareEngineer)";
        Assertions.assertEquals(List.of("Project"), NavigationUtils.getNavigationTypes(firstPath));
        Assertions.assertEquals(List.of("Project", "Sprint"), NavigationUtils.getNavigationTypes(secondPath));
        Assertions.assertEquals(List.of("Project", "Sprint", "SoftwareEngineer"), NavigationUtils.getNavigationTypes(thirdPath));
    }
}
