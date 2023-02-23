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

package ansk.development.domain.template;

import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.functions.ConstraintFunction;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Represents a template bag that can be either
 * {@link ConstraintFunction}
 * or
 * {@link Constraint}.
 * <p>
 * Every template is a predefined instance with default values.
 * <p>
 * To facilitate the process of constraint definition by end-users,
 * most of the static values are filled out automatically.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Template {
    private String uuid;
    private String functionName;
    private String functionType;

    private String description;
    private String template;

    private Template(String template) {
        this.template = template;
    }

    private Template(String functionName, String description, String functionType, String template) {
        this.uuid = UUID.randomUUID().toString();
        this.functionName = functionName;
        this.description = description;
        this.functionType = functionType;
        this.template = template;
    }

    public static Template ofFunction(String functionName, String description, String functionType, String template) {
        return new Template(functionName, description, functionType, template);
    }

    public static Template ofConstraint(String template) {
        return new Template(template);
    }
}
