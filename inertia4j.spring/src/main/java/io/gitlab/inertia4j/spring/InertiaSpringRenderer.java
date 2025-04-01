package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.HttpRequest;
import io.gitlab.inertia4j.core.HttpResponse;
import io.gitlab.inertia4j.core.InertiaRenderer;
import io.gitlab.inertia4j.core.InertiaRenderingOptions;
import io.gitlab.inertia4j.spi.PageObjectSerializer;
import io.gitlab.inertia4j.spi.TemplateRenderer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

/**
 * Bridges Spring-specific rendering with the core InertiaRenderer.
 */
class InertiaSpringRenderer {
    private final InertiaRenderer coreRenderer;

    /*
     * Constructor for InertiaSpringRenderer.
     * 
     * @param serializer PageObjectSerializer implementation used to serialize PageObject
     * @param versionProvider provider for the current Inertia asset version
     * @param templateRenderer renderer for HTML responses
     */
    public InertiaSpringRenderer(
        PageObjectSerializer serializer,
        VersionProvider versionProvider,
        TemplateRenderer templateRenderer
    ) {
        this.coreRenderer = new InertiaRenderer(serializer, versionProvider::get, templateRenderer);
    }

    /*
     * Constructor for InertiaSpringRenderer.
     * 
     * @param serializer PageObjectSerializer implementation used to serialize PageObject
     * @param versionProvider provider for the current Inertia asset version
     * @param templatePath path to the HTML template to be rendered
     */
    public InertiaSpringRenderer(
        PageObjectSerializer serializer,
        VersionProvider versionProvider,
        String templatePath
    ) {
        this.coreRenderer = new InertiaRenderer(serializer, versionProvider::get, templatePath);
    }

    /*
     * Formats the server response to the Inertia response format.
     * 
     * @param request HTTP request
     * @param options Inertia flags and other Page Object data
     */
    public ResponseEntity<String> render(
        HttpRequest request,
        InertiaRenderingOptions options
    ) {
        return convertToResponseEntity(coreRenderer.render(request, options));
    }

    /*
     * Formats the proper redirect response to the specified location.
     *
     * @param request HTTP request
     * @param location URL to redirect to
     */
    public ResponseEntity<String> redirect(HttpRequest request, String location) {
        return convertToResponseEntity(coreRenderer.redirect(request, location));
    }

    /*
     * Redirects to an external or non-Inertia URL.
     *
     * @param location external URL to redirect to
     */
    public ResponseEntity<String> location(String url) {
        return convertToResponseEntity(coreRenderer.location(url));
    }

    private ResponseEntity<String> convertToResponseEntity(HttpResponse response) {
        HttpHeaders responseHeaders = new HttpHeaders(
            CollectionUtils.toMultiValueMap(response.getHeaders())
        );
        return new ResponseEntity<>(response.getBody(), responseHeaders, response.getCode());
    }
}
