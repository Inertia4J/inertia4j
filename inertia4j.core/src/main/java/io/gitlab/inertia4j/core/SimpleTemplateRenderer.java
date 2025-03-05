package io.gitlab.inertia4j.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleTemplateRenderer implements TemplateRenderer {
    private final Matcher templateMatcher;

    public SimpleTemplateRenderer(
        String templatePath
    ) throws TemplateRenderingException {
        String template = loadTemplate(templatePath);

        this.templateMatcher = Pattern.compile("\\$\\{pageObject}").matcher(template);
    }

    @Override
    public String render(String pageObjectJson) {
        return templateMatcher.replaceAll(pageObjectJson);
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
