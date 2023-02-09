package ansk.development.mapper;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.mapper.AbstractToPSConstraintMapper;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.util.List;

public class GremlinConstraintMapper implements AbstractToPSConstraintMapper<TinkerGraph> {

    @Override
    public String mapToPlatformSpecificConstraint(Constraint constraint) {
        return null;
    }

    @Override
    public TinkerGraph mapToPlatformSpecificGraph(List<InstanceElement> instanceElementGraph) {
        TinkerGraph tinkerGraph = TinkerGraph.open();
        GraphTraversalSource graphSource = tinkerGraph.traversal();

        instanceElementGraph.forEach(instanceElement -> {
            var instanceVertex = graphSource
                    .addV(instanceElement.getInstanceOf())
                    .as(instanceElement.getUuid());

            instanceElement.getSlots().forEach(slot -> {
                instanceVertex.property(slot.getKey(), slot.getValue());
            });
            instanceVertex.iterate();
        });

        instanceElementGraph
                .stream()
                .flatMap(instanceElement -> instanceElement.getLinks().stream())
                .forEach(link -> {
                    graphSource.addE(link.getName())
                            .from(link.getInstanceUuid())
                            .to(link.getTargetInstanceUuid())
                            .iterate();
                });
        var vm = graphSource.V().elementMap().toList();
        System.out.println(vm);
        return null;
    }
}
