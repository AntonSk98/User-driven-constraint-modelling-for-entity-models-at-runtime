package de.antonsk98.development.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.antonsk98.development.domain.codi.CodiModel;
import de.antonsk98.development.domain.codi.deepInstance.*;
import de.antonsk98.development.domain.codi.model.Model;
import de.antonsk98.development.domain.codi.model.ModelElement;
import de.antonsk98.development.service.abs.Deserializer;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Concrete JSON deserializer of a JSON to {@link CodiModel}.
 *
 * @author Anton Skripin
 */
public class CodiDeserializer extends Deserializer<CodiModel> {

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public CodiModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        JsonNode topHierarchyNode = jsonParser.getCodec().readTree(jsonParser);
        Model model = deserializeModel(topHierarchyNode);
        DeepInstance deepInstance = deserializeDeepInstance(topHierarchyNode);
        return new CodiModel(model, deepInstance);
    }

    /**
     * Deserializes a part of JSON to {@link Model}.
     * @param tree top-hierarchy node.
     * @return {@link  Model}
     */
    @SneakyThrows(IOException.class)
    private Model deserializeModel(JsonNode tree) {
        JsonNode modelNode = tree.get("definition").get("model");
        Set<ModelElement> modelElementSet = new ObjectMapper()
                .readValue(modelNode.toString(), new TypeReference<>() {
                });
        return new Model(modelElementSet);
    }

    /**
     * Deserializes a part of JSON to {@link DeepInstance}.
     * @param tree top-hierarchy node.
     * @return {@link DeepInstance}
     */
    @SneakyThrows(IOException.class)
    private DeepInstance deserializeDeepInstance(JsonNode tree) {
        ObjectMapper objectMapper = new ObjectMapper();
        Set<Clabject> clabjects = new HashSet<>();
        Iterator<JsonNode> instancesIterator = tree.withArray("configuration").elements();
        while (instancesIterator.hasNext()) {
            JsonNode instance = instancesIterator.next();
            ClabjectData clabjectData = objectMapper.readValue(instance.get(0).toString(), ClabjectData.class);
            Set<ExtensionData> extensionData = objectMapper
                    .readValue(instance
                            .get(1)
                            .toString(), new TypeReference<>() {
                    });
            Set<AttributeData> attributeData = objectMapper
                    .readValue(instance
                            .get(2)
                            .toString(), new TypeReference<>() {
                    });
            Set<AssociationData> associationData = objectMapper
                    .readValue(instance
                            .get(3)
                            .toString(), new TypeReference<>() {
                    });
            clabjects.add(new Clabject(clabjectData, extensionData, attributeData, associationData));
        }
        return new DeepInstance(clabjects);
    }
}
