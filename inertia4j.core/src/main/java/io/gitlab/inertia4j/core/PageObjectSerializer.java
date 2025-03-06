package io.gitlab.inertia4j.core;

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
}
