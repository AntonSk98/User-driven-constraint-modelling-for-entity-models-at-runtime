package de.antonsk98.development.gremlin.service;

@FunctionalInterface
public interface GremlinLogicalFunction {

    String apply(Boolean isNested, String... functions);
}
