package com.tinkerpop.gremlin.structure.io.graphson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tinkerpop.gremlin.structure.Graph;
import com.tinkerpop.gremlin.util.function.FunctionUtils;

import java.io.IOException;

/**
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
class GraphSONGraph {
    private final Graph graphToSerialize;

    public GraphSONGraph(final Graph graphToSerialize) {
        this.graphToSerialize = graphToSerialize;
    }

    public Graph getGraphToSerialize() {
        return graphToSerialize;
    }

    static class GraphJacksonSerializer extends StdSerializer<GraphSONGraph> {
        public GraphJacksonSerializer() {
            super(GraphSONGraph.class);
        }

        @Override
        public void serialize(final GraphSONGraph graph, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
                throws IOException, JsonGenerationException {
            final Graph g = graph.getGraphToSerialize();
            jsonGenerator.writeStartObject();

            if (g.getFeatures().graph().supportsMemory())
                jsonGenerator.writeObjectField(GraphSONModule.TOKEN_PROPERTIES, g.memory().asMap());

            jsonGenerator.writeArrayFieldStart(GraphSONModule.TOKEN_VERTICES);
            g.V().forEach(FunctionUtils.wrapConsumer(jsonGenerator::writeObject));
            jsonGenerator.writeEndArray();

            jsonGenerator.writeArrayFieldStart(GraphSONModule.TOKEN_EDGES);
            g.E().forEach(FunctionUtils.wrapConsumer(jsonGenerator::writeObject));
            jsonGenerator.writeEndArray();

            jsonGenerator.writeEndObject();
        }
    }
}
