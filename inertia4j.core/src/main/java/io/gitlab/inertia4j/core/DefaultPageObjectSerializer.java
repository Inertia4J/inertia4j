package io.gitlab.inertia4j.core;

import java.util.List;

public class DefaultPageObjectSerializer implements PageObjectSerializer {
    private final PageObjectSerializer actualSerializer;

    public DefaultPageObjectSerializer() {
        try {
            Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
        } catch (ClassNotFoundException exception) {
            throw new MissingDependencyException("Missing Jackson JSON dependency. Please add it to the classpath or provide a custom PageObjectSerializer implementation");
        }
        this.actualSerializer = new JacksonPageObjectSerializer();
    }

    @Override
    public String serialize(PageObject pageObject, List<String> partialDataProps) throws SerializationException {
        return actualSerializer.serialize(pageObject, partialDataProps);
    }
}
