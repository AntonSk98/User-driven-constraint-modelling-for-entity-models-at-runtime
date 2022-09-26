package de.antonsk98.development.util;

import org.apache.jena.datatypes.xsd.XSDDatatype;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Callable;

/**
 * Class containing helper methods to construct an RDF model.
 *
 * @author Anton Skripin
 */
public class RdfUtils {
    /**
     *
     * @param value
     * @return
     */
    public static XSDDatatype getXsdDatatypeByValue(String value) {
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
