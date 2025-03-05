package io.gitlab.inertia4j.core;

public interface PageObjectSerializer {
    String serialize(PageObject pageObject) throws SerializationException;
}
