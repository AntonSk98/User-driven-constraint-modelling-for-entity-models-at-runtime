package anton.skripin.development.domain.integrity;

import anton.skripin.development.domain.constraint.Constraint;
import lombok.Getter;

import java.util.Set;

/**
 * Integrity report.
 */
@Getter
public class IntegrityReport {
    private String modelElement;

    private String oldPropertyName;
    private String newPropertyName;
    private String deletedPropertyName;
    private Set<Constraint> updatedConstraints;
    private Set<Constraint> deletedConstraints;

    /**
     * Constructor for integrity report as a result of updating the schema of a model element.
     * @param modelElement name of a model element
     * @param oldPropertyName old property name
     * @param newPropertyName new property name
     * @param updatedConstraints list of updated functions
     */
    public IntegrityReport(String modelElement, String oldPropertyName, String newPropertyName, Set<Constraint> updatedConstraints) {
        this.modelElement = modelElement;
        this.oldPropertyName = oldPropertyName;
        this.newPropertyName = newPropertyName;
        this.updatedConstraints = updatedConstraints;
    }

    /**
     * Constructor for integrity report as a result of deleting some elements from the schema of a model element.
     * @param modelElement name of a model element
     * @param deletedPropertyName deleted property name
     * @param deletedConstraints list of invalid functions
     */
    public IntegrityReport(String modelElement, String deletedPropertyName, Set<Constraint> deletedConstraints) {
        this.modelElement = modelElement;
        this.deletedPropertyName = deletedPropertyName;
        this.deletedConstraints = deletedConstraints;
    }
}
