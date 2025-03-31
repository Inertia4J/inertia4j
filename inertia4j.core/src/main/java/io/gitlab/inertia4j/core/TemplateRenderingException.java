package io.gitlab.inertia4j.core;

/*
 * Exception thrown when the default template renderer can't find the template file.
 */
// TODO: make this extend InertiaException
public class TemplateRenderingException extends Exception {
    public TemplateRenderingException(String path) {
        super(path, null);
    }

    public TemplateRenderingException(String path, Throwable cause) {
        super("Failed to read resource at path " + path, cause);
    }
}
