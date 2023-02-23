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

package ansk.development.exception.constraint.function;

import ansk.development.exception.constraint.ConstraintException;

/**
 * Concrete {@link ConstraintException} thrown if a function with a given name already exists.
 */
public class UniqueFunctionNameException extends FunctionException {
    public UniqueFunctionNameException(String functionName) {
        super(String.format("Function '%s' exists in the scope of templates and no duplicates are allowed!", functionName));
    }
}
