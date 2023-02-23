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

import ansk.development.domain.model.ModelElement;

/**
 * Interfaces that maps two technical model element spaces.
 * On the one side, there is the technical space of an end-user constraint engine.
 * On the other side, there is the technical space of a platform that provides means for modeling and entity management.
 *
 * @param <TargetModelElement> model element of the technical space of a modeling tool
 */
public interface ModelMapper<TargetModelElement> {
    ModelElement mapToModelElement(TargetModelElement sourceModel);
}
