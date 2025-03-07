package io.gitlab.inertia4j.core;

/*
 * Interface to get HTTP request information.
 */
public interface HttpRequest {
    /*
     * Gets the value of the HTTP request header with specified name.
     * 
     * @params name name of the request header
     * @returns value of the request header
     */
    String getHeader(String name);

    /*
     * Returns the HTTP method of the request.
     */
    String getMethod();
}
