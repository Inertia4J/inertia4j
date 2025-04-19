package io.github.inertia4j.spi;

/**
 * Interface for template renderers used by Inertia4j.
 */
public interface TemplateRenderer {
    /**
     * Renders the template and injects the JSON string of the page object in place of the <code>@PageObject@</code> placeholder.
     * The <code>@PageObject@</code> placeholder is intended to be placed as the value for the <code>data-page</code> prop in the desired SPA element within the template.
     *
     * @param pageObjectJson JSON string representation of the {@link PageObject}.
     * @return A string containing the HTML template with the {@code PageObject} data injected.
     */
    String render(String pageObjectJson);
}
