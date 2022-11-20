package de.antonsk98.development.gremlin.service;

import de.antonsk98.development.gremlin.QueryConstructor;

import java.util.HashMap;
import java.util.Map;

import static de.antonsk98.development.gremlin.service.FunctionName.*;

public class GremlinFunctionService {

    public static Map<FunctionName, GremlinBasicFunction> basicFunctionToGremlinQuery = new HashMap<>();

    public static Map<FunctionName, GremlinLogicalFunction> logicalFunctionToGremlinQuery = new HashMap<>();

    static {
        basicFunctionToGremlinQuery.put(FOCUS_PROPERTY, QueryConstructor.focusProperty());
        basicFunctionToGremlinQuery.put(GREATER_THAN, QueryConstructor.greaterThan());
        basicFunctionToGremlinQuery.put(LESS_THAN, QueryConstructor.lessThan());

        logicalFunctionToGremlinQuery.put(AND, QueryConstructor.and());
        logicalFunctionToGremlinQuery.put(OR, QueryConstructor.or());
    }
}
