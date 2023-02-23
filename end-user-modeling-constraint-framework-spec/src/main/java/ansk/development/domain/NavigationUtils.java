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

package ansk.development.domain;

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
