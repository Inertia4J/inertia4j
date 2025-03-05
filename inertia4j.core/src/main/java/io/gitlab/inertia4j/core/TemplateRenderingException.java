package io.gitlab.inertia4j.core;

public class TemplateRenderingException extends Exception {
    public TemplateRenderingException(String path) {
        super(path, null);
    }

    public TemplateRenderingException(String path, Throwable cause) {
        super("Failed to read resource at path " + path, cause);
    }
}
