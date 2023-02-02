package anton.skripin.development.domain.constraint;

import java.util.List;

/**
 * Element for integrity mechanisms.
 * Every constraint backward link must be attached to a concrete instance element.
 * It contains:
 * 1) instance element uuid to which a link is attached;
 * 2) a list of constraint for reevaluation upon the changes of the constraint;
 * 3) a reference to an instance id in the context of which those constraints are initially defined.
 * <p>
 * <p>
 * Example:
 * <p>
 * Input:
 * A<<ModelElement>> : {attributes: [a,b,c], associations: [toB]}
 * B<<ModelElement>> : {attributes: [d,e,f], associations: []}
 * Constraint-X: forAll(A.toB -> minLength(d, 10))
 * <p>
 * Instances: a1 toB b1;
 * a1 toB b2;
 * a1 toB b3;
 * <p>
 * Then: Whenever the value of 'd' attribute of the instances b1, b2, or b3 is changed,
 * the Constraint-X defined in the context of A<<ModelElement>> must be reevaluated for a1!
 * <p>
 * Therefore: Upon linking a1 to any of b* instances,
 * the following backward constraint link must be attached to any instance of b*:
 * b1: {slots: [a->value, ...], links: [], links[{ targetInstanceUuid-> b1, constraintUuid: Constraint-X, contextInstanceUuid -> a1}]}
 */
public record ConstraintBackwardLink(String targetInstanceUuid, String contextInstanceUuid, String constraintUuid) {
}
