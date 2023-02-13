package anton.skripin.development.domain;

import java.util.List;

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
}
