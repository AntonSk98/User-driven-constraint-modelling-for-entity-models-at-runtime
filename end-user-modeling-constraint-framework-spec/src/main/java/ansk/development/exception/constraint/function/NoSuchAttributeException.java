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
 * This exception is thrown whenever there is an attempt to attempt one of the generic function attributes that is not defined on a certain function level.
 */
public class NoSuchAttributeException extends FunctionException {

    public NoSuchAttributeException(String attributeName, Class clazz) {
        super(String.format("Function %s has no attribute '%s' defined!", clazz.getSimpleName(), attributeName));
    }
}
