package io.gitlab.inertia4j.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.gitlab.inertia4j.spi.PageObject;
import io.gitlab.inertia4j.spi.PageObjectSerializer;
import io.gitlab.inertia4j.spi.SerializationException;

import java.util.List;

/*
 * PageObject serializer implementation using Jackson for JSON serialization.
 */
public class JacksonPageObjectSerializer implements PageObjectSerializer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /*
     * Serializes the provided PageObject
     *
     * @param pageObject PageObject to serialize
     * @param partialDataProps list of props to be serialized, used for partial data requests, can be null
     * @returns PageObject serialized as String with the partial data properties specified
     * @throws SerializationException if any errors occur during serialization
     * @see PageObject
     */
    @Override
    public String serialize(PageObject pageObject, List<String> partialDataProps) throws SerializationException {
        try {
            ObjectNode tree = objectMapper.valueToTree(pageObject);
            if (partialDataProps != null) {
                ObjectNode propsNode = (ObjectNode) tree.get("props");
                propsNode.retain(partialDataProps);
            }
            return objectMapper.writeValueAsString(tree);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }
}
