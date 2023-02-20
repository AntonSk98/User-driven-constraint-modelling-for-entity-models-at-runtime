package ansk.development.mapper;

import ansk.development.domain.ShaclConstraint;
import ansk.development.domain.ShaclConstraintShape;
import anton.skripin.development.domain.NavigationUtils;
import anton.skripin.development.exception.constraint.GraphConstraintException;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.topbraid.shacl.vocabulary.SH;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static anton.skripin.development.domain.constraint.functions.FunctionMetadata.FUNCTION_TO_PARAMETER_NAMES;
import static anton.skripin.development.domain.constraint.functions.FunctionMetadata.FunctionNames.*;
import static anton.skripin.development.domain.constraint.functions.FunctionType.RUNTIME_FUNCTION;

/**
 * Maps function with its concrete SHACL implementation.
 */
public class ShaclFunctionMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShaclFunctionMapper.class);
    public static Map<String, Function<ShaclConstraint, Resource>> CONSTRAINTS_MAP = new HashMap<>();

    static {
        CONSTRAINTS_MAP.put(FOR_ALL, shaclConstraint -> {
            List<String> navigation = shaclConstraint
                    .navigation()
                    .orElseThrow(() -> new GraphConstraintException("ForAll() must have a navigation"));
            Resource lambdaFunction = shaclConstraint
                    .lambdaFunction()
                    .orElseThrow(() -> new GraphConstraintException("ForAll() must have a lambda function"));
            LOGGER.error("ForAll() function is tricky to implement using plain SHACL syntax. " +
                    "The problem is that SHACL must know the exact number of connected links a lambda function must conform to, in order to evaluate this constraint." +
                    "Since during the process of constraint transformation it is impossible to know this number, " +
                    "forAll() function behaves the same way as forSome() function");
            return shaclConstraint.getContext().forAll(navigation, lambdaFunction, 1, shaclConstraint.nested());
        });

        CONSTRAINTS_MAP.put(FOR_SOME, shaclConstraint -> {
            List<String> navigation = shaclConstraint
                    .navigation()
                    .orElseThrow(() -> new GraphConstraintException("ForSome() must have a navigation"));
            var lambdaFunction = shaclConstraint
                    .lambdaFunction()
                    .orElseThrow(() -> new GraphConstraintException("ForSome() must have a lambda function"));
            return shaclConstraint.getContext().forSome(navigation, lambdaFunction, shaclConstraint.nested());
        });

        CONSTRAINTS_MAP.put(FOR_NONE, shaclConstraint -> {
            List<String> navigation = shaclConstraint
                    .navigation()
                    .orElseThrow(() -> new GraphConstraintException("ForNone() must have a navigation"));
            var lambdaFunction = shaclConstraint
                    .lambdaFunction()
                    .orElseThrow(() -> new GraphConstraintException("ForNone() must have a lambda function"));
            return shaclConstraint.getContext().forNone(navigation, lambdaFunction, shaclConstraint.nested());
        });

        CONSTRAINTS_MAP.put(FOR_EXACTLY, shaclConstraint -> {
            List<String> navigation = shaclConstraint
                    .navigation()
                    .orElseThrow(() -> new GraphConstraintException("ForExactly() must have a navigation"));
            var lambdaFunction = shaclConstraint
                    .lambdaFunction()
                    .orElseThrow(() -> new GraphConstraintException("ForExactly() must have a lambda function"));
            Integer matchNumber = Integer
                    .parseInt(shaclConstraint.params()
                            .orElseThrow(() -> new GraphConstraintException("ForExactly() must have a 'match_number' parameter"))
                            .get(FUNCTION_TO_PARAMETER_NAMES.get(FOR_EXACTLY).get(0)));
            return shaclConstraint.getContext().forExactly(navigation, lambdaFunction, matchNumber, shaclConstraint.nested());
        });

        CONSTRAINTS_MAP.put(GREATER_THAN, shaclConstraint -> {
            String attribute = shaclConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("GreaterThan() must have an attribute!"));
            String value = shaclConstraint.params()
                    .orElseThrow(() -> new GraphConstraintException("GreaterThan() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(GREATER_THAN).get(0));
            return shaclConstraint.getContext().greaterThan(attribute, value, shaclConstraint.nested());
        });

        CONSTRAINTS_MAP.put(GREATER_THAN_OR_EQUALS, shaclConstraint -> {
            String attribute = shaclConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("GreaterThanOrEquals() must have an attribute!"));
            String value = shaclConstraint.params()
                    .orElseThrow(() -> new GraphConstraintException("GreaterThanOrEquals() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(GREATER_THAN_OR_EQUALS).get(0));

            return shaclConstraint.getContext().greaterThanOrEquals(attribute, value, shaclConstraint.nested());
        });

        CONSTRAINTS_MAP.put(LESS_THAN, shaclConstraint -> {
            String attribute = shaclConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("LessThan() must have an attribute!"));
            String value = shaclConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("LessThan() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(LESS_THAN).get(0));

            return shaclConstraint.getContext().lessThan(attribute, value, shaclConstraint.nested());
        });

        CONSTRAINTS_MAP.put(LESS_THAN_OR_EQUALS, shaclConstraint -> {
            String attribute = shaclConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("LessThanOrEquals() must have an attribute!"));
            String value = shaclConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("LessThanOrEquals() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(LESS_THAN_OR_EQUALS).get(0));

            return shaclConstraint.getContext().lessThanOrEquals(attribute, value, shaclConstraint.nested());
        });

        CONSTRAINTS_MAP.put(EQUALS, shaclConstraint -> {
            String attribute = shaclConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("Equals() must have an attribute!"));
            String value = shaclConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("Equals() must have a value attribute"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(EQUALS).get(0));

            return shaclConstraint.getContext().equals(attribute, value, shaclConstraint.nested());
        });

        CONSTRAINTS_MAP.put(UNIQUE, shaclConstraint -> {
            throw new GraphConstraintException("Unique() cannot be implemented using SHACL syntax!");
        });

        CONSTRAINTS_MAP.put(NOT_NULL_OR_EMPTY, shaclConstraint -> {
            String attribute = shaclConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("NotNullOrEmpty() must have an attribute!"));

            return shaclConstraint.getContext().notNullOrEmpty(attribute, shaclConstraint.nested());
        });

        CONSTRAINTS_MAP.put(MIN_CARDINALITY, shaclConstraint -> {
            var params = shaclConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("MinCardinality() must have two value attribute"));
            String association = NavigationUtils
                    .getNavigationRoot(params.get(FUNCTION_TO_PARAMETER_NAMES.get(MIN_CARDINALITY).get(0)))
                    .get(0);
            Integer minValue = Integer.valueOf(params.get(FUNCTION_TO_PARAMETER_NAMES.get(MIN_CARDINALITY).get(1)));

            return shaclConstraint.getContext().minCardinality(association, minValue);
        });

        CONSTRAINTS_MAP.put(MAX_CARDINALITY, shaclConstraint -> {
            var params = shaclConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("MaxCardinality() must have two value attribute"));
            String association = NavigationUtils
                    .getNavigationRoot(params.get(FUNCTION_TO_PARAMETER_NAMES.get(MAX_CARDINALITY).get(0)))
                    .get(0);
            Integer maxValue = Integer.valueOf(params.get(FUNCTION_TO_PARAMETER_NAMES.get(MAX_CARDINALITY).get(1)));

            return shaclConstraint.getContext().maxCardinality(association, maxValue);
        });

        CONSTRAINTS_MAP.put(MAX_LENGTH, shaclConstraint -> {
            String attribute = shaclConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("MaxLength() must have an attribute!"));
            String length = shaclConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("MaxLength() must have a parameter"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(MAX_LENGTH).get(0));

            return shaclConstraint.getContext().maxLength(attribute, length, shaclConstraint.nested());
        });

        CONSTRAINTS_MAP.put(MIN_LENGTH, shaclConstraint -> {
            String attribute = shaclConstraint
                    .attribute()
                    .orElseThrow(() -> new GraphConstraintException("MinLength() must have an attribute!"));
            String length = shaclConstraint
                    .params()
                    .orElseThrow(() -> new GraphConstraintException("MinLength() must have a parameter"))
                    .get(FUNCTION_TO_PARAMETER_NAMES.get(MIN_LENGTH).get(0));

            return shaclConstraint.getContext().minLength(attribute, length, shaclConstraint.nested());
        });

        CONSTRAINTS_MAP.put(AND, shaclConstraint -> {
            var nestedFunction = shaclConstraint
                    .nestedFunctions()
                    .orElseThrow(() -> new GraphConstraintException("And() cannot be used without a context element"));

            return shaclConstraint.getContext().and(nestedFunction, shaclConstraint.nested());
        });

        CONSTRAINTS_MAP.put(OR, shaclConstraint -> {
            var nestedFunction = shaclConstraint
                    .nestedFunctions()
                    .orElseThrow(() -> new GraphConstraintException("Or() cannot be used without a context element"));

            return shaclConstraint.getContext().or(nestedFunction, shaclConstraint.nested());
        });

        CONSTRAINTS_MAP.put(RUNTIME_FUNCTION, shaclConstraint -> {
            var pair = shaclConstraint
                    .runtimeFunction()
                    .orElseThrow(() -> new GraphConstraintException("No function is provided for runtime function"));
            String placeholder = pair.getKey();
            String runtimeFunction = pair.getValue();
            runtimeFunction = runtimeFunction.replace(placeholder, shaclConstraint.getContext().getTargetInstance());

            ShaclConstraintShape shaclConstraintShape = new ShaclConstraintShape();
            InputStream inputStream = new ByteArrayInputStream(runtimeFunction.getBytes());
            shaclConstraintShape.read(inputStream, SH.BASE_URI, FileUtils.langTurtle);
            Shapes.parse(shaclConstraintShape.getGraph());

            return shaclConstraintShape.runtimeFunction(runtimeFunction);
        });
    }
}
