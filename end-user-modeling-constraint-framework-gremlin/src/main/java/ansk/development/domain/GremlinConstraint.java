package ansk.development.domain;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GremlinConstraint {

    private GraphTraversal<?, ?> context;

    private List<String> navigation;

    private GraphTraversal<?, ?> lambdaFunction;

    private Map<String, String> params;

    private String attribute;

    private List<GraphTraversal<?, ?>> nestedFunctions;


    public Optional<GraphTraversal<?, ?>> context() {
        return Optional.ofNullable(context);
    }

    public Optional<List<String>> navigation() {
        return Optional.ofNullable(navigation);
    }

    public Optional<GraphTraversal<?, ?>> lambdaFunction() {
        return Optional.ofNullable(lambdaFunction);
    }

    public Optional<Map<String, String>> params() {
        return Optional.ofNullable(params);
    }

    public Optional<String> attribute() {
        return Optional.ofNullable(attribute);
    }

    public Optional<List<GraphTraversal<?, ?>>> nestedFunctions() {
        return Optional.ofNullable(nestedFunctions);
    }

    public GremlinConstraint setContext(GraphTraversal<?, ?> context) {
        this.context = context;
        return this;
    }

    public GremlinConstraint setNavigation(List<String> navigation) {
        this.navigation = navigation;
        return this;
    }

    public GremlinConstraint setLambdaFunction(GraphTraversal<?, ?> lambdaFunction) {
        this.lambdaFunction = lambdaFunction;
        return this;
    }

    public GremlinConstraint setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public GremlinConstraint setAttribute(String attribute) {
        this.attribute = attribute;
        return this;
    }

    public GremlinConstraint setNestedFunctions(List<GraphTraversal<?, ?>> nestedFunctions) {
        this.nestedFunctions = nestedFunctions;
        return this;
    }
}
