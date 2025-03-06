package io.gitlab.inertia4j.core;

/*
 * Base HttpResponse interface.
 */
public interface HttpResponse {
    /*
     * Sets the HTTP Header on the Response to the value provided.
     * 
     * @param name name of the header to set value for
     * @param value value of the header
     */
    void setHeader(String name, String value);

    /*
     * Writes the body content of the HTTP response.
     * 
     * @param content the content of the HTTP response
     */
    void writeBody(String content);
}
