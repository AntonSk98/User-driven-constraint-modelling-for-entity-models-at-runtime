package ansk.development.mapper;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.mapper.AbstractToPSConstraintMapper;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.util.List;
import java.util.Objects;

public class GremlinConstraintMapper implements AbstractToPSConstraintMapper<TinkerGraph> {

    @Override
    public String mapToPlatformSpecificConstraint(Constraint constraint) {
        return null;
    }

    @Override
    public TinkerGraph mapToPlatformSpecificGraph(List<InstanceElement> instanceElementGraph) {
        final String uuid = "uuid";
        TinkerGraph tinkerGraph = TinkerGraph.open();
        GraphTraversalSource graphSource = tinkerGraph.traversal();

        instanceElementGraph.forEach(instanceElement -> {
            var instanceVertex = graphSource
                    .addV(instanceElement.getInstanceOf())
                    .property("uuid", instanceElement.getUuid());

            instanceElement.getSlots().forEach(slot -> {
                instanceVertex.property(slot.getKey(), slot.getValue());
            });
            instanceVertex.iterate();
        });

        instanceElementGraph
                .stream()
                .filter(instanceElement -> Objects.nonNull(instanceElement.getLinks()))
                .flatMap(instanceElement -> instanceElement.getLinks().stream())
                .forEach(link -> {
                    if (graphSource.V().has(uuid, link.getTargetInstanceUuid()).hasNext()) {
                        graphSource.addE(link.getName())
                                .from(graphSource.V().has(uuid, link.getInstanceUuid()).next())
                                .to(graphSource.V().has(uuid, link.getTargetInstanceUuid()).next())
                                .iterate();
                    }
                });
        var vm = graphSource.V().elementMap().toList();
        System.out.println(vm);
        return null;
    }
}
