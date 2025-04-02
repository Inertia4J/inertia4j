package io.gitlab.inertia4j.core;

import io.gitlab.inertia4j.spi.InertiaException;

/**
 * Exception thrown by {@link SimpleTemplateRenderer} when it encounters an error loading or reading the template file.
 */
public class TemplateRenderingException extends InertiaException {
    /**
     * Constructs a new exception indicating the template file was not found at the specified path.
     * @param path The classpath path where the template was expected.
     */
    public TemplateRenderingException(String path) {
        super("Template file not found at classpath resource path: " + path);
    }

    /**
     * Constructs a new exception indicating an error occurred while reading the template file.
     * @param path The classpath path of the template file being read.
     * @param cause The underlying {@link java.io.IOException} that occurred.
     */
    public TemplateRenderingException(String path, Throwable cause) {
        super("Failed to read resource at path " + path, cause);
    }
}
