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

package ansk.development.domain.constraint.functions.types;

import ansk.development.exception.constraint.function.NotImplementedException;

/**
 * Specifies the instance evolution process when a transition of one instance attribute to another state
 * is only possible if the state of another attribute is satisfied by a given requirement.
 */
public class StateBasedBehavioralFunction extends LogicalFunction {

    public StateBasedBehavioralFunction() {
        super(null, null);
        throw new NotImplementedException(this.getClass().getSimpleName());
    }
}
