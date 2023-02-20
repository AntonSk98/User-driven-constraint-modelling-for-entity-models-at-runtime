package ansk.development.domain.model;

import ansk.development.domain.ValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Element that represents a relationship between two classes.
 */
@Getter
@Setter
@NoArgsConstructor
public class Association {
    private String uuid;
    private String name;
    private String navigation;
    private String multiplicity;
    private String targetModelElementUuid;
    private String targetModelElementName;

    public void setNavigation(String navigation) {
        ValidationUtils.validateNavigation(navigation);
        this.navigation = navigation;
    }
}
