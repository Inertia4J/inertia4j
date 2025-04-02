package io.gitlab.inertia4j.core;

import io.gitlab.inertia4j.spi.InertiaException;

/**
 * Exception thrown when a dependency (like Jackson) is missing from the classpath,
 * but is needed for a specific setup (like when using the default {@link PageObjectSerializer}).
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
