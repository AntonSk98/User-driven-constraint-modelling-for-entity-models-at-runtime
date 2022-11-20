package de.antonsk98.development.gremlin;

import de.antonsk98.development.gremlin.service.GremlinBasicFunction;
import de.antonsk98.development.gremlin.service.GremlinLogicalFunction;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Objects;

public class QueryConstructor {

    private static final String SEPARATOR = ".";
    private static final String EMPTY = "";

    public static GremlinBasicFunction focusProperty() {
        return (target, isNested, arguments) -> {
            if (MapUtils.isEmpty(arguments)) {
                return String.format("%shasLabel('%s')", resolveNested(isNested), target);
            }
            throw new IllegalArgumentException("'FocusProperty()' must have no arguments");
        };
    }

    public static GremlinBasicFunction greaterThan() {
        String greaterThanParam = "greaterThan";
        return (target, isNested, arguments) -> {
            if (arguments.get(greaterThanParam) == null) {
                throw new IllegalArgumentException(String.format("'GreaterThan()' function must have exactly one argument '%s'", greaterThanParam));
            }
            return String.format("%shas('%s', gt(%s))", resolveNested(isNested), target, arguments.get(greaterThanParam));
        };
    }

    public static GremlinBasicFunction lessThan() {
        String lessThanParam = "lessThan";
        return (target, isNested, arguments) -> {
            if (arguments.get(lessThanParam) == null) {
                throw new IllegalArgumentException(String.format("'LessThan()' function must have exactly one argument '%s'", lessThanParam));
            }
            return String.format("%shas('%s', lt(%s))", resolveNested(isNested), target, arguments.get(lessThanParam));
        };
    }

    public static GremlinLogicalFunction and() {
        return (isNested, functions) -> {
            if (functions.length < 2) {
                throw new IllegalArgumentException("'And()' must have at least two functions as arguments");
            }
            return String.format("%sand(%s)", resolveNested(isNested), String.join(",", functions));
        };
    }

    public static GremlinLogicalFunction or() {
        return (isNested, functions) -> {
            if (functions.length < 2) {
                throw new IllegalArgumentException("'Or()' must have at least two functions as arguments");
            }
            return String.format("%sor(%s)", resolveNested(isNested), String.join(",", functions));
        };
    }

    private static String resolveNested(boolean nested) {
        return nested ? EMPTY : SEPARATOR;
    }
}


