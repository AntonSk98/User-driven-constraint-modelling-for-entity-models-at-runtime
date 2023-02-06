package anton.skripin.development.properties;

import lombok.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * Class representing properties for a {@link anton.skripin.development.domain.template.Template}
 */
public abstract class TemplateConfigurationProperties {

    /**
     * Placeholder value used as a template for a single-valued attribute that must be replaced by an end-user.
     */
    public abstract String attributePlaceholder();

    public abstract String attributePlaceholderWithValidation();

    /**
     * Placeholder value used as a template for a navigation that must be replaced by an end-user.
     */
    public abstract String navigationPlaceholder();

    /**
     * Placeholder for a function parameter that must be replaced by an end-user.
     */
    public abstract String paramValuePlaceholder();

    /**
     * Placeholder for an array that accepts several functions.
     */
    public abstract String arrayValuePlaceholder();

    /**
     * Placeholder for an object key-value pair that must be replaced by an end-user.
     */
    public abstract ImmutablePair<String, String> objectKeyValuePlaceholder();

    /**
     * Every constraint must be provided with a name by end-user by replacing a placeholder.
     */
    public abstract String constraintNamePlaceholder();

    /**
     * Every constraint must have a meaningful message that will be returned in case of a constraint violation.
     */
    public abstract String violationMessagePlaceholder();


    /**
     * Default constructor.
     */
    public TemplateConfigurationProperties() {
    }
}
