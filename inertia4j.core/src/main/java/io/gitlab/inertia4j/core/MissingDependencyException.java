package io.gitlab.inertia4j.core;

// TODO: make this extend InertiaException
public class MissingDependencyException extends RuntimeException {
    public MissingDependencyException(String message) {
        super(message);
    }
}
