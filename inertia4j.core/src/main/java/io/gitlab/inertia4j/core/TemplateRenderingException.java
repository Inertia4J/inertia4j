package io.gitlab.inertia4j.core;

import io.gitlab.inertia4j.spi.InertiaException;

/*
 * Exception thrown when the default template renderer can't find the template file.
 */
public class TemplateRenderingException extends InertiaException {
    public TemplateRenderingException(String path) {
        super(path, null);
    }

    public TemplateRenderingException(String path, Throwable cause) {
        super("Failed to read resource at path " + path, cause);
    }
}
