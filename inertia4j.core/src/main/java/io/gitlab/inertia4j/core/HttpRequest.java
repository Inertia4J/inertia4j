package io.gitlab.inertia4j.core;

@FunctionalInterface
public interface HttpRequest {
    String getHeader(String name);
}
