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

package ansk.development.exception.constraint;

/**
 * Concrete {@link ConstraintException} thrown in case of template creation failures.
 */
public class ConstraintTemplateCreationException extends ConstraintException {

    public ConstraintTemplateCreationException() {
        super("Failed to construct a template for a constraint");
    }

    public ConstraintTemplateCreationException(String name) {
        super(String.format("Failed to construct template for method %s", name));
    }
}
