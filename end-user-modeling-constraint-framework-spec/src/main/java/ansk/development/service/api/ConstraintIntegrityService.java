package ansk.development.service.api;

import ansk.development.domain.integrity.IntegrityReport;
import ansk.development.domain.constraint.Constraint;

import java.util.List;

/**
 * Service to provide integrity mechanisms for constraints.
 */
public interface ConstraintIntegrityService {
    /**
     * Synchronize constraints after changing the schema of a model element.
     * @param modelElement name of a model element
     * @param oldPropertyName old name of changed property
     * @param newPropertyName new name of changed property
     * @param constraints list of affected constraints
     * @return {@link IntegrityReport}
     */
    IntegrityReport synchronizeConstraints(String modelElement, String oldPropertyName, String newPropertyName, List<Constraint> constraints);

    /**
     * Delivers the list of invalid functions via {@}
     * @param modelElement
     * @param removedPropertyName
     * @param constraints
     * @return
     */
    IntegrityReport getInvalidConstraints(String modelElement, String removedPropertyName, List<Constraint> constraints);
}
