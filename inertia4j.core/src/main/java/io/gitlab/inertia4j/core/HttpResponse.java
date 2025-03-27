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
     * @returns this HttpResponse instance
     */
    HttpResponse setHeader(String name, String value);

    /*
     * Sets the HTTP status code.
     *
     * @param code HTTP status code
     * @returns this HttpResponse instance
     */
    HttpResponse setCode(Integer code);

    /*
     * Writes the body content of the HTTP response.
     * 
     * @param content the content of the HTTP response
     * @returns this HttpResponse instance
     */
    HttpResponse writeBody(String content);
}
