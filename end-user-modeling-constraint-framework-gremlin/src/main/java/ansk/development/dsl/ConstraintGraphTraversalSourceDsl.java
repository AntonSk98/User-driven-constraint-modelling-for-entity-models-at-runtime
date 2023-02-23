/*
 * Copyright (c) 2023 Anton Skripin
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package ansk.development.dsl;

import ansk.development.domain.instance.InstanceElement;
import ansk.development.domain.instance.Link;
import org.apache.tinkerpop.gremlin.process.remote.RemoteConnection;
import org.apache.tinkerpop.gremlin.process.remote.RemoteConnectionException;
import org.apache.tinkerpop.gremlin.process.remote.traversal.RemoteTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.Bytecode;
import org.apache.tinkerpop.gremlin.process.traversal.TraversalStrategies;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.concurrent.CompletableFuture;

/**
 * Class that contains DSL-specific methods for Gremlin graph instantiation.
 */
public class ConstraintGraphTraversalSourceDsl extends GraphTraversalSource {

    private static final String UUID_PROPERTY = "uuid";
    private static final String NAME = "name";

    public ConstraintGraphTraversalSourceDsl(Graph graph, TraversalStrategies traversalStrategies) {
        super(graph, traversalStrategies);
    }

    public ConstraintGraphTraversalSourceDsl(Graph graph) {
        super(graph);
    }

    public ConstraintGraphTraversalSourceDsl(RemoteConnection connection) {
        super(new RemoteConnection() {
            @Override
            public <E> CompletableFuture<RemoteTraversal<?, E>> submitAsync(Bytecode bytecode) throws RemoteConnectionException {
                return null;
            }

            @Override
            public void close() throws Exception {

            }
        });
    }

    /**
     * Get instance element by uuid.
     *
     * @param uuid uuid
     * @return instance as a Vertex
     */
    public GraphTraversal<Vertex, Vertex> instance(String uuid) {
        GraphTraversal<Vertex, Vertex> traversal = this.clone().V();
        return traversal.has(UUID_PROPERTY, uuid);
    }

    /**
     * Get instance element by name.
     *
     * @param name name
     * @return instance as a Vertex
     */
    public GraphTraversal<Vertex, Vertex> instanceByName(String name) {
        GraphTraversal<Vertex, Vertex> traversal = this.clone().V();
        return traversal.has(NAME, name);
    }

    /**
     * Adds instance to the Gremlin graph.
     *
     * @param instanceElement See {@link InstanceElement}
     */
    public void addInstance(InstanceElement instanceElement) {
        var instanceVertex = this
                .addV(instanceElement.getInstanceOf())
                .property("uuid", instanceElement.getUuid());

        instanceElement.getSlots().forEach(slot -> {
            instanceVertex.property(slot.getKey(), slot.getValue());
        });
        instanceVertex.iterate();
    }

    /**
     * Links two instances in a graph by link.
     *
     * @param link See {@link Link}
     */
    public void linkTwoInstances(Link link) {
        if (doesInstanceExists(link.getTargetInstanceUuid())) {
            this.addE(link.getName())
                    .from(this.V().has(UUID_PROPERTY, link.getInstanceUuid()).next())
                    .to(this.V().has(UUID_PROPERTY, link.getTargetInstanceUuid()).next())
                    .iterate();
        }
    }

    private boolean doesInstanceExists(String uuid) {
        return this.V().has(UUID_PROPERTY, uuid).hasNext();
    }

    public boolean isValid(GraphTraversal<?, Boolean> gremlinConstraint) {
        return gremlinConstraint.next();
    }
}
