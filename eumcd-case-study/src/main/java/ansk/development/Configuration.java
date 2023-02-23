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

package ansk.development;

import ansk.development.mapper.GremlinConstraintMapper;
import ansk.development.mapper.ShaclConstraintMapper;
import ansk.development.service.GremlinConstraintValidationService;
import ansk.development.service.ShaclConstraintValidationService;

/**
 * Configuration class.
 */
public final class Configuration {

    private static final GremlinConstraintValidationService gremlinConstraintValidationService = new GremlinConstraintValidationService();
    private static final ShaclConstraintValidationService shaclConstraintValidationService = new ShaclConstraintValidationService();

    private static final GremlinConstraintMapper gremlinConstraintMapper = new GremlinConstraintMapper();
    private static final ShaclConstraintMapper shaclConstraintMapper = new ShaclConstraintMapper();

    private Configuration() {

    }

    public static GremlinConstraintValidationService gremlinConstraintValidationService() {
        return gremlinConstraintValidationService;
    }

    public static ShaclConstraintValidationService shaclConstraintValidationService() {
        return shaclConstraintValidationService;
    }

    public static GremlinConstraintMapper gremlinConstraintMapper() {
        return gremlinConstraintMapper;
    }

    public static ShaclConstraintMapper shaclConstraintMapper() {
        return shaclConstraintMapper;
    }
}
