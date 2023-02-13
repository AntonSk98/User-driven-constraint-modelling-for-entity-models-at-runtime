package anton.skripin.development.domain;

import java.util.List;

/**
 * Class to fetch full navigation path from a valid navigation
 */
public class NavigationUtils {

    public static List<String> getNavigationRoot(String navigation) {
        ValidationUtils.validateNavigation(navigation);
        String navigationWithoutTypes = navigation.replaceAll("\\(\\w+\\)", "");
        return List.of(navigationWithoutTypes.split("\\."));
    }
}
