package io.github.inertia4j.spi;

import java.util.List;

/**
 * Interface for implementing serializers for {@link PageObject} objects.
 * Used to transform the {@code PageObject} into a string representation. An implementation must be provided to the renderer.
 */
public interface PageObjectSerializer {
    /**
     * Serializes the provided {@link PageObject}.
     *
     * @param pageObject       {@code PageObject} to serialize
     * @param partialDataProps list of props to be serialized, used for partial data requests, can be null
     * @return {@code PageObject} serialized as a String
     * @throws SerializationException if any errors occur during serialization
     */
    String serialize(PageObject pageObject, List<String> partialDataProps) throws SerializationException;
}
