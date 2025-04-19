package io.github.inertia4j.core;

import io.github.inertia4j.spi.PageObject;
import io.github.inertia4j.spi.PageObjectSerializer;
import io.github.inertia4j.spi.SerializationException;

import java.util.List;

/**
 * Default implementation of {@link PageObjectSerializer}.
 * <p>
 * This implementation checks if Jackson Databind is present on the classpath.
 * If it is, it delegates serialization to {@link JacksonPageObjectSerializer}.
 * If not, it throws a {@link MissingDependencyException} during construction.
 */
public class DefaultPageObjectSerializer implements PageObjectSerializer {
    private final PageObjectSerializer actualSerializer;

    /**
     * Constructs a new DefaultPageObjectSerializer, checking for Jackson dependency.
     * @throws MissingDependencyException if Jackson Databind is not found on the classpath.
     */
    public DefaultPageObjectSerializer() {
        try {
            Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
        } catch (ClassNotFoundException exception) {
            throw new MissingDependencyException("Missing Jackson JSON dependency. Please add it to the classpath or provide a custom PageObjectSerializer implementation");
        }
        this.actualSerializer = new JacksonPageObjectSerializer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String serialize(PageObject pageObject, List<String> partialDataProps) throws SerializationException {
        return actualSerializer.serialize(pageObject, partialDataProps);
    }
}
