package io.github.inertia4j.core;

import io.github.inertia4j.spi.TemplateRenderer;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link TemplateRenderer} implementation used by default if no specific renderer is provided.
 * It loads a template file from the classpath and replaces a placeholder with the page object JSON.
 */
public class SimpleTemplateRenderer implements TemplateRenderer {
    private final Matcher templateMatcher;

    /**
     * Constructs a SimpleTemplateRenderer.
     * Loads the template from the specified classpath resource path and prepares it for rendering.
     *
     * @param templatePath Classpath path to the HTML template file (e.g., "/templates/app.html").
     * @throws TemplateRenderingException if the template file cannot be loaded or read.
     */
    public SimpleTemplateRenderer(
        String templatePath
    ) throws TemplateRenderingException {
        String template = loadTemplate(templatePath);

        this.templateMatcher = Pattern.compile("@PageObject@").matcher(template);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation replaces all occurrences of the <code>@PageObject@</code> placeholder
     * in the loaded template with the provided {@code pageObjectJson}.
     */
    @Override
    public String render(String pageObjectJson) {
        return templateMatcher.replaceAll(pageObjectJson);
    }

    /**
     * Loads the template content from the specified classpath resource path.
     * @param path The classpath path to the template file.
     * @return The content of the template file as a String.
     * @throws TemplateRenderingException if the template file cannot be found or read.
     */
    private String loadTemplate(String path) throws TemplateRenderingException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new TemplateRenderingException(path);
            }
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new TemplateRenderingException(path, e);
        }
    }
}
