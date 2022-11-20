package de.antonsk98.development.gremlin.service;

import java.util.Map;

@FunctionalInterface
public interface GremlinBasicFunction {

    String apply(String target, Boolean isNested, Map<String, String> arguments);
}
