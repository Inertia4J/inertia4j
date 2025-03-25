package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.HttpRequest;
import io.gitlab.inertia4j.core.InertiaRenderer;
import io.gitlab.inertia4j.core.InertiaRenderingOptions;
import io.gitlab.inertia4j.core.TemplateRenderingException;
import io.gitlab.inertia4j.spi.PageObjectSerializer;
import io.gitlab.inertia4j.spi.SerializationException;
import io.gitlab.inertia4j.spi.TemplateRenderer;
import org.springframework.http.ResponseEntity;

/**
 * Bridges Spring-specific rendering with the core InertiaRenderer.
 */
class InertiaSpringRenderer {
    private final InertiaRenderer renderer;

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
    ) throws SpringInertiaException {
        this.renderer = new InertiaRenderer(serializer, versionProvider::get, templateRenderer);
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
    ) throws SpringInertiaException {
        try {
            this.renderer = new InertiaRenderer(serializer, versionProvider::get, templatePath);
        } catch (TemplateRenderingException e) {
            throw new SpringInertiaException(e);
        }
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
        SpringHttpResponse inertiaSpringResponse = new SpringHttpResponse();

        try {
            renderer.render(request, inertiaSpringResponse, options);
        } catch (SerializationException e) {
            throw new SpringInertiaException(e);
        }

        return inertiaSpringResponse.toResponseEntity();
    }

    /*
     * Formats the proper redirect response to the specified location.
     *
     * @param request HTTP request
     * @param location URL to redirect to
     */
    public ResponseEntity<String> redirect(HttpRequest request, String location) {
        SpringHttpResponse inertiaSpringResponse = new SpringHttpResponse();

        renderer.redirect(request, inertiaSpringResponse, location);

        return inertiaSpringResponse.toResponseEntity();
    }

    /*
     * Redirects to an external or non-Inertia URL.
     *
     * @param location external URL to redirect to
     */
    public ResponseEntity<String> location(String url) {
        SpringHttpResponse inertiaSpringResponse = new SpringHttpResponse();

        renderer.location(inertiaSpringResponse, url);

        return inertiaSpringResponse.toResponseEntity();
    }
}
