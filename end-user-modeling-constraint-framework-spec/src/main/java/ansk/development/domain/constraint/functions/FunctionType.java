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

/**
 * Defines all possible function types.
 */
public final class FunctionType {
    public static final String LOGICAL_FUNCTION = "LOGICAL_FUNCTION";
    public static final String STRING_BASED_FUNCTION = "STRING_BASED_FUNCTION";
    public static final String COLLECTION_BASED_FUNCTION = "COLLECTION_BASED_FUNCTION";
    public static final String RANGE_BASED_FUNCTION = "RANGE_BASED_FUNCTION";
    public static final String ASSOCIATION_BASED_FUNCTION = "ASSOCIATION_BASED_FUNCTION";
    public static final String OBJECT_TEMPLATE_PLACEHOLDER = "OBJECT_TEMPLATE_PLACEHOLDER";
    public static final String RUNTIME_FUNCTION = "RUNTIME_FUNCTION";
    public static final String CONDITIONAL_BASED_FUNCTION = "CONDITIONAL_BASED_FUNCTION";
}
