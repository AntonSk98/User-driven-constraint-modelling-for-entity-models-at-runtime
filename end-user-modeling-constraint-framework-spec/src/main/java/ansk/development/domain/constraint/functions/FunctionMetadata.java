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

package ansk.development.domain.constraint.functions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ansk.development.domain.constraint.functions.FunctionMetadata.FunctionNames.*;

/**
 * Metadata class to store all supported constraint names and required attribute parameters.
 */
public class FunctionMetadata {

    public static Map<String, List<String>> FUNCTION_TO_PARAMETER_NAMES = new HashMap<>();

    static {
        FUNCTION_TO_PARAMETER_NAMES.put(MIN_LENGTH, List.of("min_length"));
        FUNCTION_TO_PARAMETER_NAMES.put(MAX_LENGTH, List.of("max_length"));
        FUNCTION_TO_PARAMETER_NAMES.put(FOR_EXACTLY, List.of("match_number"));
        FUNCTION_TO_PARAMETER_NAMES.put(GREATER_THAN, List.of("value"));
        FUNCTION_TO_PARAMETER_NAMES.put(GREATER_THAN_OR_EQUALS, List.of("value"));
        FUNCTION_TO_PARAMETER_NAMES.put(LESS_THAN, List.of("value"));
        FUNCTION_TO_PARAMETER_NAMES.put(LESS_THAN_OR_EQUALS, List.of("value"));
        FUNCTION_TO_PARAMETER_NAMES.put(EQUALS, List.of("value"));
        FUNCTION_TO_PARAMETER_NAMES.put(MIN_CARDINALITY, List.of("association", "min_value"));
        FUNCTION_TO_PARAMETER_NAMES.put(MAX_CARDINALITY, List.of("association", "max_value"));
    }

    public static class FunctionNames {
        public static final String AND = "AND";
        public static final String OR = "OR";
        public static final String FOR_ALL = "FOR_ALL";
        public static final String FOR_SOME = "FOR_SOME";
        public static final String FOR_NONE = "FOR_NONE";
        public static final String FOR_EXACTLY = "FOR_EXACTLY";
        public static final String MIN_LENGTH = "MIN_LENGTH";
        public static final String MAX_LENGTH = "MAX_LENGTH";
        public static final String GREATER_THAN = "GREATER_THAN";
        public static final String GREATER_THAN_OR_EQUALS = "GREATER_THAN_OR_EQUALS";
        public static final String LESS_THAN = "LESS_THAN";
        public static final String LESS_THAN_OR_EQUALS = "LESS_THAN_OR_EQUALS";
        public static final String EQUALS = "EQUALS";
        public static final String UNIQUE = "UNIQUE";
        public static final String NOT_NULL_OR_EMPTY = "NOT_NULL_OR_EMPTY";
        public static final String MIN_CARDINALITY = "MIN_CARDINALITY";
        public static final String MAX_CARDINALITY = "MAX_CARDINALITY";
        public static final String IF_THEN = "IF_THEN";
        public static final String IF_THEN_ELSE = "IF_THEN_ELSE";
    }
}
