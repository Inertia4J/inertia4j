package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.*;
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
     * @param templateRenderer renderer for HTML responses
     */
    public InertiaSpringRenderer(
        PageObjectSerializer serializer,
        TemplateRenderer templateRenderer
    ) throws SpringInertiaException {
        this.renderer = new InertiaRenderer(serializer, templateRenderer);
    }

    /*
     * Constructor for InertiaSpringRenderer.
     * 
     * @param serializer PageObjectSerializer implementation used to serialize PageObject
     * @param templatePath path to the HTML template to be served
     */
    public InertiaSpringRenderer(
        PageObjectSerializer serializer,
        String templatePath
    ) throws SpringInertiaException {
        try {
            this.renderer = new InertiaRenderer(serializer, templatePath);
        } catch (TemplateRenderingException e) {
            throw new SpringInertiaException(e);
        }
    }

    /*
     * Formats the server response to the Inertia response format.
     * 
     * @param headerGetter request header getter
     * @param url value of the URL field in response
     * @param component name of the component to render in the client
     * @param props regular response data
     */
    public ResponseEntity<String> render(
        RequestHeaderGetter headerGetter,
        InertiaRenderingOptions options
    ) {
        SpringHttpResponse inertiaSpringResponse = new SpringHttpResponse();

        try {
            renderer.render(headerGetter, inertiaSpringResponse, options);
        } catch (SerializationException e) {
            throw new SpringInertiaException(e);
        }

        return inertiaSpringResponse.toResponseEntity();
    }
}
