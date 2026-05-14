package io.github.inertia4j.springboot4;

import io.github.inertia4j.spi.PageObject;
import io.github.inertia4j.spi.PageObjectSerializer;
import io.github.inertia4j.spi.SerializationException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * {@link PageObjectSerializer} implementation using Jackson 3 for JSON serialization.
 */
@NullMarked
class Jackson3PageObjectSerializer implements PageObjectSerializer {
    private final ObjectMapper objectMapper = JsonMapper.builder()
        .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
        .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
        .build();

    /**
     * {@inheritDoc}
     * <p>
     * If {@code partialDataProps} is provided, only the properties specified
     * in the list will be included under the "props" key in the resulting JSON.
     */
    @Override
    public String serialize(
        PageObject pageObject,
        @Nullable List<String> partialDataProps
    ) throws SerializationException {
        try {
            ObjectNode tree = objectMapper.valueToTree(pageObject);
            if (partialDataProps != null) {
                ObjectNode propsNode = (ObjectNode) tree.get("props");
                propsNode.retain(partialDataProps);
            }
            return objectMapper.writeValueAsString(tree);
        } catch (JacksonException e) {
            throw new SerializationException(e);
        }
    }
}
