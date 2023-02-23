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

package ansk.development.domain.constraint.backward_links;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * If a constraint is added or updated it might refer to other model elements via navigation.
 * E.g. works_on(Project).consists_of(Sprint) -> this navigation inside a constraint function refers to two external types.
 * <p>
 * In this case, whenever the schema of either Project or Sprint is updated, a constraint might become invalid.
 * E.g. association name in Project is changed from 'consist_of' to 'comprises'.
 * <p>
 * Therefore, both Project and Sprint will have a reference to this constraint.
 * Whenever, the schema of those type is updated, the referenced constraints must be checked for their integrity definition.
 * <p>
 * For this purpose, model element backward links are introduced.
 */
@Getter
@AllArgsConstructor
public class ModelElementBackwardLink {
    String targetModelElement;
    String contextModelElement;
    String constraintUuid;
}
