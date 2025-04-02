package io.gitlab.inertia4j.core;

/**
 * Interface providing access to HTTP request information needed by Inertia4j.
 */
public interface HttpRequest {
    /**
     * Gets the value of the HTTP request header with the specified name.
     *
     * @param name name of the request header.
     * @return value of the request header, or {@code null} if not found.
     */
    String getHeader(String name);

    /**
     * Returns the HTTP method of the request (e.g., "GET", "POST").
     * @return The HTTP method as a String.
     */
    String getMethod();
}
