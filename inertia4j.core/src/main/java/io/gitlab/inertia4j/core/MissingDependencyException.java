package io.gitlab.inertia4j.core;

public class MissingDependencyException extends RuntimeException {
    public MissingDependencyException(String message) {
        super(message);
    }
}
