package ansk.development.dsl;

import ansk.development.exception.GraphConstraintException;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.TextP;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.GremlinDsl;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains constraint functions written for Gremlin dialect.
 *
 * @param <S> input graph node type
 * @param <E> output graph node type
 */
@GremlinDsl(traversalSource = "ansk.development.dsl.ConstraintGraphTraversalSourceDsl")
public interface ConstraintGraphTraversalDsl<S, E> extends GraphTraversal.Admin<S, E> {
    Logger LOGGER = LoggerFactory.getLogger(ConstraintGraphTraversalDsl.class);

    /**
     * For every instance that emerge as a result of navigation check that a given constraint holds.
     *
     * @param navigation     navigation
     * @param lambdaFunction constraint that must be resolved to true
     * @return true if all constraints are valid
     */
    default GraphTraversal<S, Boolean> forAll(List<String> navigation, GraphTraversal<?, Boolean> lambdaFunction) {
        for (String type : navigation) {
            out(type);
        }
        lambdaFunction.asAdmin().getSteps().forEach(step -> this.asAdmin().addStep(step));
        return is(P.eq(false)).count().map(traverser -> traverser.get() == 0);
    }

    /**
     * For at least one instance that emerge as a result of navigation check that a given constraint holds.
     *
     * @param navigation     navigation
     * @param lambdaFunction constraint that must be resolved to true
     * @return true if at least one constraint is valid
     */
    default GraphTraversal<S, Boolean> forSome(List<String> navigation, GraphTraversal<S, Boolean> lambdaFunction) {
        for (String type : navigation) {
            out(type);
        }
        lambdaFunction.asAdmin().getSteps().forEach(step -> this.asAdmin().addStep(step));
        return is(P.eq(true)).count().map(traverser -> traverser.get() > 0);
    }

    /**
     * For all the instances that emerge as a result of the navigation checks that a given constraint does not hold.
     *
     * @param navigation     navigation
     * @param lambdaFunction constraint that must be resolved to false
     * @return true if all instances do not satisfy a constraint
     */
    default GraphTraversal<S, Boolean> forNone(List<String> navigation, GraphTraversal<S, Boolean> lambdaFunction) {
        for (String type : navigation) {
            out(type);
        }
        lambdaFunction.asAdmin().getSteps().forEach(step -> this.asAdmin().addStep(step));
        return is(P.eq(true)).count().map(traverser -> traverser.get() == 0);
    }

    /**
     * Among all the instances that emerge as a result of the navigation checks that a given constraint holds exactly N times.
     *
     * @param navigation     navigation
     * @param lambdaFunction constraint that must be resolved to true N times
     * @param matches        number of time a constraint must match
     * @return true if number of constraint matches equals to a given parameter
     */
    default GraphTraversal<S, Boolean> forExactly(List<String> navigation, GraphTraversal<S, Boolean> lambdaFunction, int matches) {
        for (String type : navigation) {
            out(type);
        }
        lambdaFunction.asAdmin().getSteps().forEach(step -> this.asAdmin().addStep(step));
        return is(P.eq(true)).count().map(traverser -> traverser.get() == matches);
    }

    /**
     * The length of a value of a given property must be greater or equals than a minimum length.
     *
     * @param attribute attribute
     * @param length    minimum length
     * @return true if constraint is valid
     */
    default GraphTraversal<S, Boolean> minLength(String attribute, String length) {
        return values(attribute).map(traverser -> {
            String value = (String) traverser.get();
            boolean valid = value != null && value.length() >= Integer.parseInt(length);
            LOGGER.info("MinLength({}, {}) -> valid: {}:", attribute, value, valid);
            return valid;
        });
    }

    /**
     * The length of a value of a given property must be less or equals than an allowed maximum length.
     *
     * @param attribute attribute
     * @param length    maximum length
     * @return true if constraint is valid
     */
    default GraphTraversal<S, Boolean> maxLength(String attribute, String length) {
        return values(attribute).map(traverser -> {
            String value = (String) traverser.get();
            boolean valid = value != null && value.length() <= Integer.parseInt(length);
            LOGGER.info("MaxLength({}, {}) -> valid: {}", attribute, value, valid);
            return valid;
        });
    }

    /**
     * Checks whether the value for a given attribute is greater than a given value.
     *
     * @param attribute attribute
     * @param value     value
     * @return true if constraint is valid
     */
    default GraphTraversal<S, Boolean> greaterThan(String attribute, String value) {
        return values(attribute).map(traverser -> {
            boolean valid = Long.parseLong(String.valueOf(traverser.get())) > Long.parseLong(value);
            LOGGER.info("GreaterThan({}, {}) -> valid: {}", attribute, value, valid);
            return valid;
        });
    }

    /**
     * Checks whether the value for a given attribute is greater or equals than a given value.
     *
     * @param attribute attribute
     * @param value     value
     * @return true if constraint is valid
     */
    default GraphTraversal<S, Boolean> greaterThanOrEquals(String attribute, String value) {
        return values(attribute).map(traverser -> {
            boolean valid = Long.parseLong(String.valueOf(traverser.get())) >= Long.parseLong(value);
            LOGGER.info("GreaterThanOrEquals({}, {}) -> valid: {}", attribute, value, valid);
            return valid;
        });
    }

    /**
     * Checks whether the value for a given attribute is less than a given value.
     *
     * @param attribute attribute
     * @param value     value
     * @return true if constraint is valid
     */
    default GraphTraversal<S, Boolean> lessThan(String attribute, String value) {
        return values(attribute).map(traverser -> {
            boolean valid = Long.parseLong(String.valueOf(traverser.get())) < Long.parseLong(value);
            LOGGER.info("LessThan({}, {}) -> valid: {}", attribute, value, valid);
            return valid;
        });
    }

    /**
     * Checks whether the value for a given attribute is less or equals than a given value.
     *
     * @param attribute attribute
     * @param value     value
     * @return true if constraint is valid
     */
    default GraphTraversal<S, Boolean> lessThanOrEquals(String attribute, String value) {
        return values(attribute).map(traverser -> {
            boolean valid = Long.parseLong(String.valueOf(traverser.get())) <= Long.parseLong(value);
            LOGGER.info("LessThanOrEquals({}, {}) -> valid: {}", attribute, value, valid);
            return valid;
        });
    }

    /**
     * Checks whether the value for a given attribute is equal a given value.
     *
     * @param attribute attribute
     * @param value     value
     * @return true if constraint is valid
     */
    default GraphTraversal<S, Boolean> equals(String attribute, String value) {
        return values(attribute).map(traverser -> {
            boolean valid = traverser.get().equals(value);
            LOGGER.info("Equals({}, {}) -> valid: {}", attribute, value, valid);
            return valid;
        });
    }

    /**
     * Composite function that takes multiple nested constraints.
     *
     * @param nestedFunctions list of nested constraints
     * @return true if all nested constraints are evaluated to true
     */
    default GraphTraversal<S, Boolean> and(List<GraphTraversal<S, Boolean>> nestedFunctions) {
        List<Boolean> results = new ArrayList<>();
        for (GraphTraversal<S, Boolean> nestedFunction : nestedFunctions) {
            GraphTraversal.Admin<S, E> nestedResult = this.asAdmin().clone();
            if (nestedResult.getGraph().isEmpty() || nestedResult.getGraph().equals(EmptyGraph.instance())) {
                throw new GraphConstraintException("AND() function must be applied within a context!");
            }
            nestedFunction.asAdmin().getSteps().forEach(nestedResult::addStep);
            nestedResult.tryNext().ifPresent(e -> results.add((Boolean) e));
        }
        return this.asAdmin().constant(results).map(listTraverser -> listTraverser.get().stream().allMatch(value -> value));
    }

    /**
     * Composite function that takes multiple nested constraints.
     *
     * @param nestedFunctions list of nested constraints
     * @return true if at least one among nested constraints is evaluated to true
     */
    default GraphTraversal<S, Boolean> or(List<GraphTraversal<S, Boolean>> nestedFunctions) {
        List<Boolean> results = new ArrayList<>();
        for (GraphTraversal<S, Boolean> nestedFunction : nestedFunctions) {
            GraphTraversal.Admin<S, E> nestedResult = this.asAdmin().clone();
            if (nestedResult.getGraph().isEmpty() || nestedResult.getGraph().equals(EmptyGraph.instance())) {
                throw new GraphConstraintException("OR() function must be applied within a context!");
            }
            nestedFunction.asAdmin().getSteps().forEach(nestedResult::addStep);
            nestedResult.tryNext().ifPresent(e -> results.add((Boolean) e));
        }
        return this.asAdmin().constant(results).map(listTraverser -> listTraverser.get().stream().anyMatch(value -> value));
    }

    /**
     * Checks whether the value for a given attribute does not exist within a related subgraph.
     *
     * @param attribute attribute
     * @return true if a value for an attribute is unique
     */
    default GraphTraversal<S, Boolean> unique(String attribute) {
        if (this.asAdmin().getGraph().isEmpty() || this.asAdmin().getGraph().get().equals(EmptyGraph.instance())) {
            throw new GraphConstraintException("Unique function can only be used within a context!");
        }
        GraphTraversal<S, E> context = this.asAdmin().as("context");
        String contextValue = String.valueOf(this.asAdmin().clone().values(attribute).tryNext().orElse(""));
        String type = context.asAdmin().clone().label().tryNext().orElse("");
        return this
                .asAdmin()
                .V()
                .hasLabel(type)
                .has(attribute, contextValue)
                .count()
                .map(traverser -> {
                    boolean valid = traverser.get() == 1;
                    LOGGER.info("For {} unique({}) -> valid: {}", type, attribute, valid);
                    return valid;
                });
    }

    /**
     * Checks whether the value for a given property is neither null nor empty.
     *
     * @param attribute attribute
     * @return true if a constraint is valid
     */
    default GraphTraversal<S, Boolean> notNullOrEmpty(String attribute) {
        return and(__.has(attribute), __.values(attribute).is(TextP.neq("")))
                .count()
                .map(traverser -> {
                    boolean valid = traverser.get() > 0;
                    LOGGER.info("NotNullOrEmpty({}) -> valid: {}", attribute, valid);
                    return valid;
                });
    }

    /**
     * Limits the number of elements a context instance can be associated width -> upper bound.
     *
     * @param outType  to association
     * @param minValue minimum allowed number of instances to be associated with
     * @return true if a constraint is valid
     */
    default GraphTraversal<S, Boolean> minCardinality(String outType, int minValue) {
        return out(outType).count().map(traverser -> {
            boolean valid = traverser.get() >= minValue;
            LOGGER.info("MinCardinality({}, {}) -> valid: {}", outType, minValue, valid);
            return valid;
        });
    }

    /**
     * Limits the number of elements a context instance can be associated width -> lower bound.
     *
     * @param outType  to association
     * @param maxValue maximum allowed number of instances to be associated with
     * @return true if a constraint is valid
     */
    default GraphTraversal<S, Boolean> maxCardinality(String outType, int maxValue) {
        return out(outType).count().map(traverser -> {
            boolean valid = traverser.get() <= maxValue;
            LOGGER.info("MaxCardinality({}, {}) -> valid: {}", outType, maxValue, valid);
            return valid;
        });
    }
}
