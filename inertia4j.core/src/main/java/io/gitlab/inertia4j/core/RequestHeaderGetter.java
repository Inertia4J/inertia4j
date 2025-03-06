package io.gitlab.inertia4j.core;

/*
 * Interface to get HTTP request headers by name.
 */
@FunctionalInterface
public interface RequestHeaderGetter {
    /*
     * Gets the value of the HTTP request header with specified name.
     * 
     * @params name name of the request header
     * @returns value of the request header
     */
    String get(String name);
}
