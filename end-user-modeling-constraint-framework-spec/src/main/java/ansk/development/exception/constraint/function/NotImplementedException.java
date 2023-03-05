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

/**
 * Exception to indicate that this function is not implemented in the initial version of specification.
 */
public class NotImplementedException extends FunctionException {

    public NotImplementedException(String functionName) {
        super(String.format("Function '%s' is not implemented!", functionName));
    }
}
