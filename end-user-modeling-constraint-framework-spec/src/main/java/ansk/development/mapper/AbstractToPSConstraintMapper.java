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

package ansk.development.mapper;

import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.instance.InstanceElement;

import java.util.List;

/**
 * Interface that must be implemented by a specific constraint engine.
 * It maps the abstract syntax definition of a {@link Constraint} to a platform specific one.
 */
public interface AbstractToPSConstraintMapper<PlatformSpecificGraph, PlatformSpecificConstraint> {
    PlatformSpecificConstraint mapToPlatformSpecificConstraint(String uuid, Constraint constraint);

    PlatformSpecificGraph mapToPlatformSpecificGraph(List<InstanceElement> subgraphForValidation);
}
