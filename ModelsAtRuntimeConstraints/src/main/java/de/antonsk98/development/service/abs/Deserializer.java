package de.antonsk98.development.service.abs;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.gson.JsonParseException;

/**
 * Abstract deserializer of a JSON model.
 * @param <T> target model to which a JSON should be deserialized.
 *           
 * @author Anton Skripin
 */
public abstract class Deserializer<T> extends JsonDeserializer<T> {

    /**
     * {@inheritDoc}
     */
    public abstract T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JsonParseException;
}
