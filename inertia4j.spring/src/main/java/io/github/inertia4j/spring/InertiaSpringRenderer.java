package io.github.inertia4j.spring;

import io.github.inertia4j.core.HttpRequest;
import io.github.inertia4j.core.HttpResponse;
import io.github.inertia4j.core.InertiaRenderer;
import io.github.inertia4j.core.InertiaRenderingOptions;
import io.github.inertia4j.spi.PageObjectSerializer;
import io.github.inertia4j.spi.TemplateRenderer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

/**
 * Spring-specific renderer that wraps the core {@link InertiaRenderer}.
 * It takes results from the core renderer (which produces a generic {@link HttpResponse})
 * and converts them into Spring's {@link ResponseEntity} objects.
 */
class InertiaSpringRenderer {
    private final InertiaRenderer coreRenderer;

    /**
     * Constructs an InertiaSpringRenderer with explicit dependencies.
     *
     * @param serializer       PageObjectSerializer implementation used to serialize the {@link io.github.inertia4j.spi.PageObject}.
     * @param versionProvider provider for the current Inertia asset version
     * @param templateRenderer renderer for the base HTML template used in full page loads.
     */
    public InertiaSpringRenderer(
        PageObjectSerializer serializer,
        VersionProvider versionProvider,
        TemplateRenderer templateRenderer
    ) {
        this.coreRenderer = new InertiaRenderer(serializer, versionProvider::get, templateRenderer);
    }

    /**
     * Constructs an InertiaSpringRenderer using the default {@link io.github.inertia4j.core.SimpleTemplateRenderer}.
     *
     * @param serializer       PageObjectSerializer implementation used to serialize the {@link io.github.inertia4j.spi.PageObject}.
     * @param versionProvider provider for the current Inertia asset version
     * @param templatePath path to the HTML template to be rendered
     * @throws io.github.inertia4j.core.TemplateRenderingException if the template file cannot be read.
     */
    public InertiaSpringRenderer(
        PageObjectSerializer serializer,
        VersionProvider versionProvider,
        String templatePath
    ) {
        this.coreRenderer = new InertiaRenderer(serializer, versionProvider::get, templatePath);
    }

    /**
     * Renders the response according to the Inertia protocol and converts it to a {@link ResponseEntity}.
     * Handles full page loads, partial updates, and asset version conflicts.
     *
     * @param request The incoming HTTP request wrapper.
     * @param options Inertia flags and other Page Object data
     * @return A Spring {@link ResponseEntity} containing the Inertia response.
     */
    public ResponseEntity<String> render(
        HttpRequest request,
        InertiaRenderingOptions options
    ) {
        return convertToResponseEntity(coreRenderer.render(request, options));
    }

    /**
     * Creates an appropriate Inertia redirect response and converts it to a {@link ResponseEntity}.
     * Uses a 303 See Other redirect for PUT/PATCH/DELETE requests and a 302 Found for others.
     *
     * @param request The incoming HTTP request wrapper.
     * @param location URL to redirect to
     * @return A Spring {@link ResponseEntity} configured for an Inertia redirect.
     */
    public ResponseEntity<String> redirect(HttpRequest request, String location) {
        return convertToResponseEntity(coreRenderer.redirect(request, location));
    }

    /**
     * Creates an external redirect response (409 Conflict + X-Inertia-Location header)
     * and converts it to a {@link ResponseEntity}.
     *
     * @param url The external URL to redirect to.
     * @return A Spring {@link ResponseEntity} configured for an external Inertia redirect.
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
