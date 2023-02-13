package ansk.development.mapper;

import ansk.development.GremlinRegistry;
import ansk.development.domain.GremlinConstraint;
import ansk.development.dsl.ConstraintGraphTraversalSource;
import ansk.development.exception.GraphTransformationException;
import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.mapper.AbstractToPSConstraintMapper;

import java.util.List;
import java.util.Objects;

public class GremlinConstraintMapper implements AbstractToPSConstraintMapper<ConstraintGraphTraversalSource> {

    @Override
    public ConstraintGraphTraversalSource mapToPlatformSpecificConstraint(String uuid, Constraint constraint) {
        GremlinConstraint gremlinConstraint = new GremlinConstraint();
        gremlinConstraint.setContext(GremlinRegistry.getConstraintTraversal().instance(uuid));
        ConstraintFunction constraintFunction = constraint.getConstraintFunction();
        constraintFunction.attribute().ifPresent();
        return null;
    }

    @Override
    public ConstraintGraphTraversalSource mapToPlatformSpecificGraph(List<InstanceElement> instanceElementGraph) {
        GremlinRegistry.spawnNewGraph();
        ConstraintGraphTraversalSource graphSource = GremlinRegistry.getConstraintTraversal();

        try {
            instanceElementGraph.forEach(graphSource::addInstance);
            instanceElementGraph
                    .stream()
                    .filter(instanceElement -> Objects.nonNull(instanceElement.getLinks()))
                    .flatMap(instanceElement -> instanceElement.getLinks().stream())
                    .forEach(graphSource::linkTwoInstances);
        } catch (Exception e) {
            throw new GraphTransformationException("Error occurred while constructing a gremlin graph", e);
        }

        return graphSource;
    }


}
