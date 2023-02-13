package anton.skripin.development.domain;

import anton.skripin.development.domain.ValidationUtils;

/**
 * Class to fetch attribute name from a valid attribute
 */
public class AttributeUtils {

    /**
     * Converts specification-based attribute into its pure form.
     *
     * from <Type>attribute to attribute
     */
    public static String getAttributeRoot(String attribute) {
        ValidationUtils.validateAttribute(attribute);
        return attribute.replaceAll("<\\w+>", "");
    }
}
