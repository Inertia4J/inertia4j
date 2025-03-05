package io.gitlab.inertia4j.core;

@FunctionalInterface
public interface RequestHeaderGetter {
    String get(String name);
}
