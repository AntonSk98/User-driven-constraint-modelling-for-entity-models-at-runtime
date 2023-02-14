package ansk.development.mapper;

import ansk.development.domain.GremlinConstraint;
import ansk.development.dsl.__;
import ansk.development.exception.GraphConstraintException;
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
            List<String> navigation = gremlinConstraint.navigation().orElseThrow(() -> new GraphConstraintException("ForAll() must have a navigation"));
            var lambdaFunction = gremlinConstraint.lambdaFunction().orElseThrow(() -> new GraphConstraintException("ForAll() must have a lambda function"));
            if (gremlinConstraint.context().isPresent()) {
                return gremlinConstraint.context().get().forAll(navigation, lambdaFunction);
            } else {
                return __.forAll(navigation, lambdaFunction);
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
    }
}
