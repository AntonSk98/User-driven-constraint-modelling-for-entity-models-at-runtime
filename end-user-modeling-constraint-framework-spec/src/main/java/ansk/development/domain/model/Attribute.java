package ansk.development.domain.model;

import ansk.development.domain.ValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a data definition for an instance of a classifier
 */
@Getter
@Setter
@NoArgsConstructor
public class Attribute {
    private String uuid;
    private String key;
    private String datatype;
    private String attribute;
    private String navigation;
    private boolean isMultiValued;

    public void setAttribute(String attribute) {
        ValidationUtils.validateAttribute(attribute);
        this.attribute = attribute;
    }

    public void setNavigation(String navigation) {
        ValidationUtils.validateNavigation(navigation);
        this.navigation = navigation;
    }
}
