package anton.skripin.development.service.api;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.backward_links.InstanceBackwardLink;
import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import anton.skripin.development.domain.constraint.backward_links.ModelElementBackwardLink;

import java.util.List;
import java.util.Map;

/**
 * Provides necessary methods to persist and manage stored constraints.
 */
public interface ConstraintPersistenceService {

    /**
     * Save constraint by provided model element id.
     *
     * @param id         of a model element a constraint should be associated with
     * @param constraint to be persisted
     * @return true if successfully saved
     */
    boolean saveConstraint(String id, Constraint constraint);

    /**
     * By adding a new constraint this function analyzes what other types this constraint is referencing to.
     * If it finds external references to other model elements via navigation, it adds them to model element backward link space.
     * @param constraint {@link Constraint}
     * @param constraintFunction {@link ConstraintFunction}
     */
    void resolveModelElementBackwardLinks(Constraint constraint, ConstraintFunction constraintFunction);


    /**
     * Adds model element backward link.
     * @param targetModelElement target model element name
     * @param contextModelElement context model element
     * @param constraintUuid uuid of a constraint
     * @return true if persisted
     */
    boolean addModelElementBackwardLink(String targetModelElement, String contextModelElement, String constraintUuid);

    /**
     * Fetches all constraint for a required model element type.
     *
     * @param type of a model element
     * @return {@link Constraint}
     */
    List<Constraint> getAllConstraints(String type);


    /**
     * Returns all constraints associated with a model element type via {@link ModelElementBackwardLink}.
     * @param type model element name
     * @return list of associated {@link Constraint}
     */
    List<Constraint> getAllBackwardLinkConstraints(String type);

    /**
     * Fetches all constraints of a required name of a certain model element type.
     *
     * @param type of a model element
     * @param name of a required constraint
     * @return list of {@link Constraint}
     */
    List<Constraint> getAllConstraintsByName(String type, String name);

    /**
     * Fetches a constraints by its unique identification number.
     *
     * @param uuid of constraint
     * @return {@link Constraint}
     */
    Constraint getConstraintByUuid(String uuid);

    /**
     * Returns all constraints for a type and all its supertypes.
     * This function is added as a support foe modeling frameworks supporting deep modeling.
     *
     * @param typeToSupertypeMap map where key is a type and value are all its supertypes.
     *                           E.g. {'SoftwareEngineer': [Employee, Person, Entity]}
     * @return map of a type and all constraints associated with it including inherited constraints
     */
    Map<String, List<Constraint>> getGroupConstraintsByTypeAnsSupertypes(Map<String, List<String>> typeToSupertypeMap);

    /**
     * Removes a constraint by its identity number.
     *
     * @param uuid of a {@link Constraint}
     * @return true if {@link Constraint} is successfully removed
     */
    boolean removeConstraintByUuid(String uuid);

    /**
     * Whenever a constraint is removed all its reference in {@link ModelElementBackwardLink} must also be removed.
     * @param uuid of a constraint
     */
    void removeModelElementBackwardLinkByConstraintUuid(String uuid);

    /**
     * Get all associated instance backward links by an instance uuid.
     *
     * @param instanceUuid of an instance
     * @return list of {@link InstanceBackwardLink}
     */
    List<InstanceBackwardLink> getConstraintLinksByInstanceUuid(String instanceUuid);

    /**
     * Adds a {@link InstanceBackwardLink} to an instance element.
     *
     * @param targetInstanceUuid  of an instance that is associated with the link and triggers constraint check on its every change
     * @param contextInstanceUuid of an instance in what context the constraint is defined
     * @param constraintUuids     of a {@link Constraint}
     * @return true if linking is successful
     */
    boolean linkConstraintToInstance(String targetInstanceUuid, String contextInstanceUuid, String constraintUuids);

    /**
     * Removes a given Constraint from an instance linking space by its uuid.
     *
     * @param targetInstanceUuid of an instance that is associated with the link and triggers constraint check on its every change
     * @param constraintUuid     of a {@link Constraint}
     * @return true if found and removed
     */
    boolean removeConstraintLinkFromInstance(String targetInstanceUuid, String constraintUuid);

    /**
     * Checks whether a given constraint is linked with an instance element via {@link InstanceBackwardLink}.
     *
     * @param targetInstanceUuid of an instance that is associated with the link and triggers constraint check on its every change
     * @param constraintUuid     of a {@link Constraint}
     * @return
     */
    boolean doesConstraintLinkExist(String targetInstanceUuid, String constraintUuid);
}
