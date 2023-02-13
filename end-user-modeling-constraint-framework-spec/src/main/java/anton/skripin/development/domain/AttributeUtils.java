package anton.skripin.development.domain;

/**
 * Class to fetch attribute name from a valid attribute
 */
public class AttributeUtils {

    /**
     * Converts specification-based attribute into its pure form.
     * <p>
     * from <Type>attribute to attribute
     *
     * @param attribute attribute
     * @return attribute root
     */
    public static String getAttributeRoot(String attribute) {
        ValidationUtils.validateAttribute(attribute);
        return attribute.replaceAll("<\\w+>", "");
    }
}
