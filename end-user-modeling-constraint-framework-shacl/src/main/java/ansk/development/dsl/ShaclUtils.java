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

package ansk.development.dsl;

import org.apache.jena.datatypes.xsd.XSDDatatype;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Callable;

/**
 * SHACL-realted utility classes.
 */
public class ShaclUtils {

    /**
     * Function to determine the datatype of SHACL property value at runtime depending on a given input.
     * @param value input
     * @return SHACL datatype
     */
    protected static XSDDatatype getXsdDatatypeByValue(String value) {
        if (successfulParse(() -> Integer.parseInt(value))) {
            return XSDDatatype.XSDint;
        }
        if (successfulParse(() -> Double.parseDouble(value))) {
            return XSDDatatype.XSDdouble;
        }
        if (successfulParse(() -> LocalDate.parse(value))) {
            return XSDDatatype.XSDdate;
        }
        if (successfulParse(() -> LocalTime.parse(value))) {
            return XSDDatatype.XSDtime;
        }
        if (successfulParse(() -> LocalDateTime.parse(value))) {
            return XSDDatatype.XSDdateTime;
        }
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return XSDDatatype.XSDboolean;
        }
        return XSDDatatype.XSDstring;
    }

    private static boolean successfulParse(Callable<?> consumer) {
        try {
            consumer.call();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
