package io.gitlab.inertia4j.core;

import java.util.List;

/*
 * Interface to implement serializers for PageObject objects.
 * Used to transform the PageObject into a string, an implementation must be provided to the renderer.
 */
public interface PageObjectSerializer {
    /*
     * Serializes the provided PageObject
     *
     * @param pageObject PageObject to serialize
     * @returns PageObject serialized as String
     * @throws SerializationException if any errors occur during serialization
     * @see PageObject
     */
    String serialize(PageObject pageObject) throws SerializationException;

    /*
     * Serializes the provided PageObject
     *
     * @param pageObject PageObject to serialize
     * @param partialDataProps list of props to be serialized, used for partial data requests, can be null
     * @returns PageObject serialized as String
     * @throws SerializationException if any errors occur during serialization
     * @see PageObject
     */
    String serialize(PageObject pageObject, List<String> partialDataProps) throws SerializationException;
}
