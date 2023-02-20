package ansk.development.domain;

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
