package io.gitlab.inertia4j.core;

/*
 * Interface for template renderers.
 */
public interface TemplateRenderer {
    /*
     * Renders the template and injects the JSON string of the page object in place of the '${pageObject}' placeholder.
     * The '${pageObject}' placeholder is intended to be placed as value for the data-page prop in the desired SPA element in the template.
     * 
     * @props pageObjectJson JSON string of the page object
     * @returns string containing the HTML template with the PageObject data
     */
    String render(String pageObjectJson);
}
