package io.github.inertia4j.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a simple HTTP response, holding the status code, headers, and body.
 * Used internally by InertiaRenderer to construct the final response.
 */
public class HttpResponse {
    private int code;
    private final Map<String, List<String>> headers = new HashMap<>();
    private String body;

    /**
     * Sets the HTTP status code for the response.
     * @param code The HTTP status code.
     * @return This HttpResponse instance for chaining.
     */
    HttpResponse setCode(int code) {
        this.code = code;
        return this;
    }

    /**
     * Adds an HTTP header to the response. If the header already exists, the value is added to the list of values for that header.
     * @param name The name of the header.
     * @param value The value of the header.
     * @return This HttpResponse instance for chaining.
     */
    HttpResponse setHeader(String name, String value) {
        headers.putIfAbsent(name, new ArrayList<>());
        headers.get(name).add(value);
        return this;
    }

    /**
     * Sets the body content for the response.
     * @param body The response body as a String.
     * @return This HttpResponse instance for chaining.
     */
    HttpResponse setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * Gets the HTTP status code of the response.
     * @return The HTTP status code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets the headers of the response.
     * @return A map containing the response headers, where the key is the header name and the value is a list of header values.
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * Gets the body of the response.
     * @return The response body as a String.
     */
    public String getBody() {
        return body;
    }
}
