package io.gitlab.inertia4j.spring;

/**
 * Functional interface for providing the current Inertia asset version.
 * Implementations of this interface can be provided as Spring beans to customize version handling.
 */
public interface VersionProvider {
    /**
     * @return The current Inertia asset version as a String, or {@code null} if versioning is not used.
     */
    String get();
}
