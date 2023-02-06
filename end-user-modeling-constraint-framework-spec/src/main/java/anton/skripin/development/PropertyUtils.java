package anton.skripin.development;

import anton.skripin.development.domain.constraint.functions.FunctionDescription;
import anton.skripin.development.properties.DefaultTemplateConfigurationProperties;

import java.io.IOException;
import java.util.Properties;

/**
 * Utility class to read external properties.
 */
public class PropertyUtils {

    /**
     * Gets default properties for {@link FunctionDescription}.
     *
     * @return {@link Properties}
     */
    public static Properties getFunctionToDescriptionProperties() {
        return getPropertiesByFileName("/properties/function_to_description.properties");
    }


    /**
     * Gets default placeholders for {@link DefaultTemplateConfigurationProperties}.
     *
     * @return
     */
    public static Properties getPlaceholderProperties() {
        return getPropertiesByFileName("/properties/placeholder.properties");
    }

    private static Properties getPropertiesByFileName(String filename) {
        Properties properties = new Properties();
        try {
            properties.load(PropertyUtils.class.getResourceAsStream(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
