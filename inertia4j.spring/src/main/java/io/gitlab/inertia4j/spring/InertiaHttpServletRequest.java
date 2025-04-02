package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.HttpRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Adapter between the Servlet request object and the core Inertia4j request.
 */
class InertiaHttpServletRequest implements HttpRequest {
    private final HttpServletRequest request;

    /**
     * Constructs a new adapter instance.
     * @param request The underlying {@link HttpServletRequest}.
     */
    public InertiaHttpServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMethod() {
        return request.getMethod();
    }
}
