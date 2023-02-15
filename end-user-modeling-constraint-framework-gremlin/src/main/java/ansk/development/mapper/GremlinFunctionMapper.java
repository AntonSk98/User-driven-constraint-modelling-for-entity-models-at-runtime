package ansk.development.mapper;

import ansk.development.domain.GremlinConstraint;
import ansk.development.dsl.__;
import ansk.development.exception.GraphConstraintException;
import anton.skripin.development.domain.NavigationUtils;
import org.apache.groovy.internal.util.Function;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static anton.skripin.development.domain.constraint.functions.FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES;
import static anton.skripin.development.domain.constraint.functions.FunctionMetadata.FunctionNames.*;

public class GremlinFunctionMapper {
    public static Map<String, Function<GremlinConstraint, GraphTraversal<?, Boolean>>> CONSTRAINTS_MAP = new HashMap<>();

    static {
        CONSTRAINTS_MAP.put(FOR_ALL, gremlinConstraint -> {
            List<String> navigation = gremlinConstraint.navigation().orElseThrow(() -> new GraphConstraintException("ForAll() must have a navigation"));
            var lambdaFunction = gremlinConstraint.lambdaFunction().orElseThrow(() -> new GraphConstraintException("ForAll() must have a lambda function"));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().forAll(navigation, lambdaFunction);
            } else {
                return __.forAll(navigation, lambdaFunction);
            }
        });

        CONSTRAINTS_MAP.put(FOR_SOME, gremlinConstraint -> {
            List<String> navigation = gremlinConstraint.navigation().orElseThrow(() -> new GraphConstraintException("ForSome() must have a navigation"));
            var lambdaFunction = gremlinConstraint.lambdaFunction().orElseThrow(() -> new GraphConstraintException("ForSome() must have a lambda function"));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().forSome(navigation, lambdaFunction);
            } else {
                return __.forSome(navigation, lambdaFunction);
            }
        });

        CONSTRAINTS_MAP.put(FOR_NONE, gremlinConstraint -> {
            List<String> navigation = gremlinConstraint.navigation().orElseThrow(() -> new GraphConstraintException("ForNone() must have a navigation"));
            var lambdaFunction = gremlinConstraint.lambdaFunction().orElseThrow(() -> new GraphConstraintException("ForNone() must have a lambda function"));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().forNone(navigation, lambdaFunction);
            } else {
                return __.forNone(navigation, lambdaFunction);
            }
        });

        CONSTRAINTS_MAP.put(FOR_EXACTLY, gremlinConstraint -> {
            List<String> navigation = gremlinConstraint.navigation().orElseThrow(() -> new GraphConstraintException("ForExactly() must have a navigation"));
            var lambdaFunction = gremlinConstraint.lambdaFunction().orElseThrow(() -> new GraphConstraintException("ForExactly() must have a lambda function"));
            Integer matchNumber = Integer.parseInt(gremlinConstraint.params()
                    .orElseThrow(() -> new GraphConstraintException("ForExactly() must have a 'match_number' parameter"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(FOR_EXACTLY).get(0)));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().forExactly(navigation, lambdaFunction, matchNumber);
            } else {
                return __.forExactly(navigation, lambdaFunction, matchNumber);
            }
        });

        CONSTRAINTS_MAP.put(GREATER_THAN, gremlinConstraint -> {
            String attribute = gremlinConstraint.attribute().orElseThrow(() -> new GraphConstraintException("GreaterThan() must have an attribute!"));
            String value = gremlinConstraint.params()
                    .orElseThrow(() -> new GraphConstraintException("GreaterThan() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(GREATER_THAN).get(0));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().greaterThan(attribute, value);
            } else {
                return __.greaterThan(attribute, value);
            }
        });

        CONSTRAINTS_MAP.put(GREATER_THAN_OR_EQUALS, gremlinConstraint -> {
            String attribute = gremlinConstraint.attribute().orElseThrow(() -> new GraphConstraintException("GreaterThanOrEquals() must have an attribute!"));
            String value = gremlinConstraint.params()
                    .orElseThrow(() -> new GraphConstraintException("GreaterThanOrEquals() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(GREATER_THAN_OR_EQUALS).get(0));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().greaterThanOrEquals(attribute, value);
            } else {
                return __.greaterThanOrEquals(attribute, value);
            }
        });

        CONSTRAINTS_MAP.put(LESS_THAN, gremlinConstraint -> {
            String attribute = gremlinConstraint.attribute().orElseThrow(() -> new GraphConstraintException("LessThan() must have an attribute!"));
            String value = gremlinConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("LessThan() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(LESS_THAN).get(0));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().lessThan(attribute, value);
            } else {
                return __.lessThan(attribute, value);
            }
        });

        CONSTRAINTS_MAP.put(LESS_THAN_OR_EQUALS, gremlinConstraint -> {
            String attribute = gremlinConstraint.attribute().orElseThrow(() -> new GraphConstraintException("LessThanOrEquals() must have an attribute!"));
            String value = gremlinConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("LessThanOrEquals() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(LESS_THAN_OR_EQUALS).get(0));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().lessThanOrEquals(attribute, value);
            } else {
                return __.lessThanOrEquals(attribute, value);
            }
        });

        CONSTRAINTS_MAP.put(EQUALS, gremlinConstraint -> {
            String attribute = gremlinConstraint.attribute().orElseThrow(() -> new GraphConstraintException("Equals() must have an attribute!"));
            String value = gremlinConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("Equals() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(EQUALS).get(0));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().equals(attribute, value);
            } else {
                return __.equals(attribute, value);
            }
        });

        CONSTRAINTS_MAP.put(UNIQUE, gremlinConstraint -> {
            String attribute = gremlinConstraint.attribute().orElseThrow(() -> new GraphConstraintException("Unique() must have an attribute!"));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().unique(attribute);
            } else {
                throw new GraphConstraintException("This function cannot be used without target context!");
            }
        });

        CONSTRAINTS_MAP.put(NOT_NULL_OR_EMPTY, gremlinConstraint -> {
            String attribute = gremlinConstraint.attribute().orElseThrow(() -> new GraphConstraintException("NotNullOrEmpty() must have an attribute!"));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().notNullOrEmpty(attribute);
            } else {
                return __.notNullOrEmpty(attribute);
            }
        });

        CONSTRAINTS_MAP.put(MIN_CARDINALITY, gremlinConstraint -> {
            var params = gremlinConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("MinCardinality() must have two value attribute"));
            String association = NavigationUtils
                    .getNavigationRoot(params.get(FUNCTION_TO_PARAMETER_NAMES.get(MIN_CARDINALITY).get(0)))
                    .get(0);
            Integer minValue = Integer.valueOf(params.get(FUNCTION_TO_PARAMETER_NAMES.get(MIN_CARDINALITY).get(1)));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().minCardinality(association, minValue);
            } else {
                throw new GraphConstraintException("This function cannot be used without target context!");
            }
        });

        CONSTRAINTS_MAP.put(MAX_CARDINALITY, gremlinConstraint -> {
            var params = gremlinConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("MaxCardinality() must have two value attribute"));
            String association = NavigationUtils
                    .getNavigationRoot(params.get(FUNCTION_TO_PARAMETER_NAMES.get(MAX_CARDINALITY).get(0)))
                    .get(0);
            Integer maxValue = Integer.valueOf(params.get(FUNCTION_TO_PARAMETER_NAMES.get(MAX_CARDINALITY).get(1)));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().maxCardinality(association, maxValue);
            } else {
                throw new GraphConstraintException("This function cannot be used without target context!");
            }
        });

        CONSTRAINTS_MAP.put(MAX_LENGTH, gremlinConstraint -> {
            String attribute = gremlinConstraint.attribute().orElseThrow(() -> new GraphConstraintException("MaxLength() must have an attribute!"));
            String length = gremlinConstraint.params().orElseThrow(() -> new GraphConstraintException("MaxLength() must have a parameter")).get(FUNCTION_TO_PARAMETER_NAMES.get(MAX_LENGTH).get(0));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().maxLength(attribute, length);
            } else {
                return __.maxLength(attribute, length);
            }
        });

        CONSTRAINTS_MAP.put(MIN_LENGTH, gremlinConstraint -> {
            String attribute = gremlinConstraint.attribute().orElseThrow(() -> new GraphConstraintException("MinLength() must have an attribute!"));
            String length = gremlinConstraint.params().orElseThrow(() -> new GraphConstraintException("MinLength() must have a parameter")).get(FUNCTION_TO_PARAMETER_NAMES.get(MIN_LENGTH).get(0));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().minLength(attribute, length);
            } else {
                return __.minLength(attribute, length);
            }
        });

        CONSTRAINTS_MAP.put(AND, gremlinConstraint -> {
            var context = gremlinConstraint.context().orElseThrow(() -> new GraphConstraintException("And() cannot be used without a context element"));
            var nestedFunction = gremlinConstraint.nestedFunctions().orElseThrow(() -> new GraphConstraintException("And() cannot be used without a context element"));
            return context.and(nestedFunction);
        });

        CONSTRAINTS_MAP.put(OR, gremlinConstraint -> {
            var context = gremlinConstraint.context().orElseThrow(() -> new GraphConstraintException("Or() cannot be used without a context element"));
            var nestedFunction = gremlinConstraint.nestedFunctions().orElseThrow(() -> new GraphConstraintException("Or() cannot be used without a context element"));
            return context.or(nestedFunction);
        });
    }
}
