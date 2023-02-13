package ansk.development;

import ansk.development.dsl.ConstraintGraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class GremlinRegistry {
    private static TinkerGraph tinkerGraph;

    public static TinkerGraph spawnNewGraph() {
        if (tinkerGraph == null) {
            tinkerGraph = TinkerGraph.open();
        } else {
            tinkerGraph.clear();
        }
        return tinkerGraph;
    }

    public static ConstraintGraphTraversalSource getConstraintTraversal() {
        if (tinkerGraph == null) {
            return spawnNewGraph().traversal(ConstraintGraphTraversalSource.class);
        } else {
            return tinkerGraph.traversal(ConstraintGraphTraversalSource.class);
        }
    }


}
