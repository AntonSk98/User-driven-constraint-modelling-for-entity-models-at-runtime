package anton.skripin.development.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to fetch full navigation path from a valid navigation
 */
public class NavigationUtils {

    /**
     * Converts specification-based navigation into the list of associations.
     * From works_on(Project).consists_of(Sprint) to List.of("works_on", "consists_of")
     *
     * @param navigation navigation
     * @return list of associations
     */
    public static List<String> getNavigationRoot(String navigation) {
        ValidationUtils.validateNavigation(navigation);
        String navigationWithoutTypes = navigation.replaceAll("\\(\\w+\\)", "");
        return List.of(navigationWithoutTypes.split("\\."));
    }

    /**
     * Retrieves all navigation types from a specification-based navifation.
     * From works_on(Project).consists_of(Sprint) to List.of("Project","Sprint")
     *
     * @param navigation navigation
     * @return list of types in a navigation
     */
    public static List<String> getNavigationTypes(String navigation) {
        ValidationUtils.validateNavigation(navigation);
        return Arrays.stream(StringUtils.substringsBetween(navigation, "(", ")")).collect(Collectors.toList());
    }
}
