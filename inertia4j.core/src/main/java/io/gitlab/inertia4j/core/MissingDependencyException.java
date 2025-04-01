package io.gitlab.inertia4j.core;

import io.gitlab.inertia4j.spi.InertiaException;

public class MissingDependencyException extends InertiaException {
    public MissingDependencyException(String message) {
        super(message);
    }
}
