package io.github.inertia4j.core;

import io.github.inertia4j.spi.InertiaException;

/**
 * Exception thrown when a dependency (like Jackson) is missing from the classpath,
 * but is needed for a specific setup (like when using the default PageObjectSerializer).
 */
public class MissingDependencyException extends InertiaException {
    /**
     * Constructs a new MissingDependencyException with the specified detail message.
     * @param message the detail message.
     */
    public MissingDependencyException(String message) {
        super(message);
    }
}
