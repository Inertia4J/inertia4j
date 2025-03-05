package io.gitlab.inertia4j.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Template renderer to be used by default when no template renderer is provided by user.
 * 
 * @see TemplateRenderer
 */
public class SimpleTemplateRenderer implements TemplateRenderer {
    private final Matcher templateMatcher;

    /*
     * Constructor for SimpleTemplateRenderer.
     * 
     * @params templatePath path to the HTML template
     * @throws TemplateRenderingException if there is a problem on template loading or compilation
     */
    public SimpleTemplateRenderer(
        String templatePath
    ) throws TemplateRenderingException {
        String template = loadTemplate(templatePath);

        this.templateMatcher = Pattern.compile("@PageObject@").matcher(template);
    }

    /*
     * Renders the template and injects the JSON string of the page object in place of the '${pageObject}' placeholder.
     * The '${pageObject}' placeholder is intended to be placed as value for the data-page prop in the desired SPA element in the template.
     * 
     * @props pageObjectJson JSON string of the page object
     * @returns string containing the HTML template with the PageObject data
     */
    @Override
    public String render(String pageObjectJson) {
        return templateMatcher.replaceAll(pageObjectJson);
    }

    /*
     * Loads the template specified by path from resources.
     * 
     * @params path path to the HTML template
     * @returns HTML template as string
     * @throws TemplateRenderingException if there is an error reading template at specified path
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
