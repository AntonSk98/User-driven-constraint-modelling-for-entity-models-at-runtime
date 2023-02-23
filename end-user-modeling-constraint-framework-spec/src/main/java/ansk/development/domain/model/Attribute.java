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
 * Represents a data definition for an instance of a classifier
 */
@Getter
@Setter
@NoArgsConstructor
public class Attribute {
    private String uuid;
    private String key;
    private String datatype;
    private String attribute;
    private String navigation;
    private boolean isMultiValued;

    public void setAttribute(String attribute) {
        ValidationUtils.validateAttribute(attribute);
        this.attribute = attribute;
    }

    public void setNavigation(String navigation) {
        ValidationUtils.validateNavigation(navigation);
        this.navigation = navigation;
    }
}
