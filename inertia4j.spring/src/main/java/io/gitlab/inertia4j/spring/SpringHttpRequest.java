package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.HttpRequest;
import jakarta.servlet.http.HttpServletRequest;

public class SpringHttpRequest implements HttpRequest {
    private final HttpServletRequest request;

    public SpringHttpRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }
}
