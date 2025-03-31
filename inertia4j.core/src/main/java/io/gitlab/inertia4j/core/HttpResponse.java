package io.gitlab.inertia4j.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Simple response data class.
 */
public class HttpResponse {
    private int code;
    private final Map<String, List<String>> headers = new HashMap<>();
    private String body;

    HttpResponse setCode(int code) {
        this.code = code;
        return this;
    }

    HttpResponse setHeader(String name, String value) {
        headers.putIfAbsent(name, new ArrayList<>());
        headers.get(name).add(value);
        return this;
    }

    HttpResponse setBody(String body) {
        this.body = body;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
