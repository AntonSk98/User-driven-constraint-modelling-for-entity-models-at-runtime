package anton.skripin.development.properties;

import anton.skripin.development.PropertyUtils;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Properties;

/**
 * Provides default placeholders for {@link TemplateConfigurationProperties}.
 */
@Getter
public class DefaultTemplateConfigurationProperties extends TemplateConfigurationProperties {

    Properties properties = PropertyUtils.getPlaceholderProperties();

    @Override
    public String attributePlaceholder() {
        return properties.getProperty("ATTRIBUTE");
    }

    @Override
    public String attributePlaceholderWithValidation() {
        return properties.getProperty("ATTRIBUTE_WITH_NAVIGATION");
    }

    @Override
    public String navigationPlaceholder() {
        return properties.getProperty("NAVIGATION");
    }

    @Override
    public String paramValuePlaceholder() {
        return properties.getProperty("PARAMETERS");
    }

    @Override
    public String arrayValuePlaceholder() {
        return properties.getProperty("ARRAY_VALUE");
    }

    @Override
    public ImmutablePair<String, String> objectKeyValuePlaceholder() {
        return new ImmutablePair<>(properties.getProperty("OBJECT_KEY"), properties.getProperty("OBJECT_VALUE"));
    }

    @Override
    public String constraintNamePlaceholder() {
        return properties.getProperty("CONSTRAINT_NAME");
    }

    @Override
    public String violationMessagePlaceholder() {
        return properties.getProperty("VIOLATION_MESSAGE");
    }

    @Override
    public String runtimeFunctionPlaceholder() {
        return properties.getProperty("RUNTIME_FUNCTION_PLACEHOLDER");
    }
}
