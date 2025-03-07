package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.HttpRequest;
import jakarta.servlet.http.HttpServletRequest;

class InertiaHttpServletRequest implements HttpRequest {
    private final HttpServletRequest request;

    public InertiaHttpServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }

    @Override
    public String getMethod() {
        return request.getMethod();
    }
}
