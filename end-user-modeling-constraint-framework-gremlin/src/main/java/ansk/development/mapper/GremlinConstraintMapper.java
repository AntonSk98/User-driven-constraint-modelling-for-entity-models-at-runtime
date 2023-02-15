package ansk.development.mapper;

import ansk.development.GremlinRegistry;
import ansk.development.domain.GremlinConstraint;
import ansk.development.dsl.ConstraintGraphTraversalSource;
import ansk.development.exception.GraphTransformationException;
import anton.skripin.development.domain.AttributeUtils;
import anton.skripin.development.domain.NavigationUtils;
import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.mapper.AbstractToPSConstraintMapper;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;

import java.util.List;
import java.util.Objects;

public class GremlinConstraintMapper implements AbstractToPSConstraintMapper<ConstraintGraphTraversalSource, GraphTraversal<?, Boolean>> {

    @Override
    public GraphTraversal<?, Boolean> mapToPlatformSpecificConstraint(String uuid, Constraint constraint) {
        ConstraintFunction constraintFunction = constraint.getConstraintFunction();
        return mapFunction(uuid, constraintFunction, true);
    }

    private GraphTraversal<?, Boolean> mapFunction(String uuid, ConstraintFunction constraintFunction, boolean traversalStart) {
        GremlinConstraint gremlinConstraint = new GremlinConstraint();
        if (traversalStart || constraintFunction.booleanFunctions().isPresent()) {
            gremlinConstraint.setContext(GremlinRegistry.getConstraintTraversal().instance(uuid));
        }
        constraintFunction.attribute().map(AttributeUtils::getAttributeRoot).ifPresent(gremlinConstraint::setAttribute);
        constraintFunction.navigation().map(NavigationUtils::getNavigationRoot).ifPresent(gremlinConstraint::setNavigation);
        constraintFunction.lambdaFunction().ifPresent(lambdaFunction -> gremlinConstraint.setLambdaFunction(mapFunction(uuid, lambdaFunction, false)));
        constraintFunction.booleanFunctions().ifPresent(booleanFunctions -> {
            booleanFunctions.forEach(booleanFunction -> gremlinConstraint.addNestedFunction(mapFunction(uuid, booleanFunction, false)));
        });
        constraintFunction.params().ifPresent(gremlinConstraint::setParams);
        gremlinConstraint.setTraversal(GremlinFunctionMapper.CONSTRAINTS_MAP.get(constraintFunction.getName()).apply(gremlinConstraint));
        return gremlinConstraint.getTraversal();
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
