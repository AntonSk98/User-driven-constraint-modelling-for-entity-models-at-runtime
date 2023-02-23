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

package ansk.development.domain.model;

import ansk.development.domain.ValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Element that represents a relationship between two classes.
 */
@Getter
@Setter
@NoArgsConstructor
public class Association {
    private String uuid;
    private String name;
    private String navigation;
    private String multiplicity;
    private String targetModelElementUuid;
    private String targetModelElementName;

    public void setNavigation(String navigation) {
        ValidationUtils.validateNavigation(navigation);
        this.navigation = navigation;
    }
}
