package io.gitlab.inertia4j.core;

import java.io.IOException;
import java.io.InputStream;

public class SimpleTemplateRenderer implements TemplateRenderer {
    private final String template;

    public SimpleTemplateRenderer(
        String templatePath
    ) throws TemplateRenderingException {
        this.template = loadTemplate(templatePath);
    }

    @Override
    public String render(String pageObjectJson) {
        return template.replaceAll("\\$\\{pageObject}", pageObjectJson);
    }

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
