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

import ansk.development.exception.constraint.function.FunctionException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Class that stores a description for every function.
 */
public class FunctionDescription {


    private static final Map<String, Object> FUNCTION_TO_DESCRIPTION_MAP = new HashMap<>();

    static {
        Properties properties = new Properties();
        try {
            properties.load(FunctionDescription.class.getResourceAsStream("/properties/function_to_description.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.AND, properties.get(FunctionMetadata.FunctionNames.AND));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.OR, properties.get(FunctionMetadata.FunctionNames.OR));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.MIN_LENGTH, properties.get(FunctionMetadata.FunctionNames.MIN_LENGTH));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.MAX_LENGTH, properties.get(FunctionMetadata.FunctionNames.MAX_LENGTH));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.FOR_ALL, properties.get(FunctionMetadata.FunctionNames.FOR_ALL));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.FOR_SOME, properties.get(FunctionMetadata.FunctionNames.FOR_SOME));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.FOR_NONE, properties.get(FunctionMetadata.FunctionNames.FOR_NONE));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.FOR_EXACTLY, properties.get(FunctionMetadata.FunctionNames.FOR_EXACTLY));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.GREATER_THAN, properties.get(FunctionMetadata.FunctionNames.GREATER_THAN));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.GREATER_THAN_OR_EQUALS, properties.get(FunctionMetadata.FunctionNames.GREATER_THAN_OR_EQUALS));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.LESS_THAN, properties.get(FunctionMetadata.FunctionNames.LESS_THAN));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.LESS_THAN_OR_EQUALS, properties.get(FunctionMetadata.FunctionNames.LESS_THAN_OR_EQUALS));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.EQUALS, properties.get(FunctionMetadata.FunctionNames.EQUALS));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.UNIQUE, properties.get(FunctionMetadata.FunctionNames.UNIQUE));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.NOT_NULL_OR_EMPTY, properties.get(FunctionMetadata.FunctionNames.NOT_NULL_OR_EMPTY));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.MIN_CARDINALITY, properties.get(FunctionMetadata.FunctionNames.MIN_CARDINALITY));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.MAX_CARDINALITY, properties.get(FunctionMetadata.FunctionNames.MAX_CARDINALITY));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.IF_THEN, properties.get(FunctionMetadata.FunctionNames.IF_THEN));
        FUNCTION_TO_DESCRIPTION_MAP.put(FunctionMetadata.FunctionNames.IF_THEN_ELSE, properties.get(FunctionMetadata.FunctionNames.IF_THEN_ELSE));
    }

    private FunctionDescription() {

    }

    public static String descriptionByName(String functionName) {
        if (FUNCTION_TO_DESCRIPTION_MAP.containsKey(functionName)) {
            return FUNCTION_TO_DESCRIPTION_MAP.get(functionName).toString();
        }
        throw new FunctionException("No description is found for " + functionName + " function!");
    }
}
