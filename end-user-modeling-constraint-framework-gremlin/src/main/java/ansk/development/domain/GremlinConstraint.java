package ansk.development.domain;

import ansk.development.dsl.ConstraintGraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GremlinConstraint {

    private ConstraintGraphTraversal<?, ?> context;

    private List<String> navigation;

    private GraphTraversal<?, Boolean> traversal;

    private GraphTraversal<?, Boolean> lambdaFunction;

    private Map<String, String> params;

    private String attribute;

    private List<GraphTraversal<?, Boolean>> nestedFunctions;


    public Optional<ConstraintGraphTraversal<?, ?>> context() {
        return Optional.ofNullable(context);
    }

    public Optional<List<String>> navigation() {
        return Optional.ofNullable(navigation);
    }

    public Optional<GraphTraversal<?, Boolean>> lambdaFunction() {
        return Optional.ofNullable(lambdaFunction);
    }

    public Optional<Map<String, String>> params() {
        return Optional.ofNullable(params);
    }

    public Optional<String> attribute() {
        return Optional.ofNullable(attribute);
    }

    public Optional<List<GraphTraversal<?, Boolean>>> nestedFunctions() {
        return Optional.ofNullable(nestedFunctions);
    }

    public void setContext(ConstraintGraphTraversal<?, ?> context) {
        this.context = context;
    }

    public void setNavigation(List<String> navigation) {
        this.navigation = navigation;
    }

    public void setLambdaFunction(GraphTraversal<?, Boolean> lambdaFunction) {
        this.lambdaFunction = lambdaFunction;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void addNestedFunction(GraphTraversal<?, Boolean> nestedFunction) {
        if (this.nestedFunctions == null) {
            this.nestedFunctions = new ArrayList<>();
        }
        this.nestedFunctions.add(nestedFunction);
    }

    public void setTraversal(GraphTraversal<?, Boolean> traversal) {
        this.traversal = traversal;
    }

    public GraphTraversal<?, Boolean> getTraversal() {
        return traversal;
    }
}
