package io.gitlab.inertia4j.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gitlab.inertia4j.core.PageObject;
import io.gitlab.inertia4j.core.PageObjectSerializer;
import io.gitlab.inertia4j.core.SerializationException;

/*
 * PageObject serializer implementation using Jackson for JSON serialization.
 */
public class JacksonPageObjectSerializer implements PageObjectSerializer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /*
     * Serializes the provided PageObject using Jackson as JSON serializer
     * 
     * @param pageObject PageObject to serialize
     * @returns PageObject JSON serialized as String
     * @throws SerializationException if any errors occur during serialization
     */
    @Override
    public String serialize(PageObject pageObject) throws SerializationException {
        try {
            return objectMapper.writeValueAsString(pageObject);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }
}
