package ansk.development.domain;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Container class to transform abstract constraint to its concrete SHACL implementation.
 */
public class ShaclConstraint {

    private ShaclConstraintShape context;
    private Boolean nested;
    private List<String> navigation;
    private Resource lambdaFunction;
    private Map<String, String> params;
    private String attribute;
    private Pair<String, String> runtimeFunction;
    private List<Resource> nestedFunctions;

    public Boolean nested() {
        return nested;
    }

    public Optional<List<String>> navigation() {
        return Optional.ofNullable(navigation);
    }

    public Optional<Resource> lambdaFunction() {
        return Optional.ofNullable(lambdaFunction);
    }

    public Optional<Map<String, String>> params() {
        return Optional.ofNullable(params);
    }

    public Optional<String> attribute() {
        return Optional.ofNullable(attribute);
    }

    public Optional<Pair<String, String>> runtimeFunction() {
        return Optional.ofNullable(runtimeFunction);
    }

    public Optional<List<Resource>> nestedFunctions() {
        return Optional.ofNullable(nestedFunctions);
    }

    public void setNested(Boolean nested) {
        this.nested = nested;
    }

    public void setNavigation(List<String> navigation) {
        this.navigation = navigation;
    }

    public void setLambdaFunction(Resource lambdaFunction) {
        this.lambdaFunction = lambdaFunction;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setRuntimeFunction(String runtimeFunction) {
        this.runtimeFunction = new ImmutablePair<>("###", runtimeFunction);
    }

    public void addNestedFunction(Resource nestedFunction) {
        if (this.nestedFunctions == null) {
            this.nestedFunctions = new ArrayList<>();
        }
        this.nestedFunctions.add(nestedFunction);
    }

    public ShaclConstraintShape getContext() {
        return context;
    }

    public void setContext(ShaclConstraintShape context) {
        this.context = context;
    }
}
