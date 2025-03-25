package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.PageObjectSerializer;
import io.gitlab.inertia4j.core.TemplateRenderer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

public class InertiaSpring {
    private final InertiaSpringRenderer renderer;
    private static final InertiaSpringRendererOptions defaultOptions = new InertiaSpringRendererOptions();

    public InertiaSpring(
        VersionProvider versionProvider,
        PageObjectSerializer pageObjectSerializer,
        TemplateRenderer templateRenderer
    ) {
        this.renderer = new InertiaSpringRenderer(pageObjectSerializer, versionProvider, templateRenderer);
    }

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @params component name of the component to render in the client
     * @param props regular response data
     */
    public ResponseEntity<String> render(String component, Map<String, Object> props) {
        return render(component, props, getCurrentRequest().getRequestURI());
    }

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @params component name of the component to render in the client
     * @param props regular response data
     * @param url value of the URL field in response
     */
    public ResponseEntity<String> render(String component, Map<String, Object> props, String url) {
        return render(getCurrentRequest(), component, props, url, defaultOptions);
    }

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @params component name of the component to render in the client
     * @param props regular response data
     * @param options Inertia flags and other Page Object data
     */
    public ResponseEntity<String> render(
        String component,
        Map<String, Object> props,
        InertiaSpringRendererOptions options
    ) {
        return render(component, props, getCurrentRequest().getRequestURI(), options);
    }

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @param url value of the URL field in response
     * @params component name of the component to render in the client
     * @param props regular response data
     * @param options Inertia flags and other Page Object data
     */
    public ResponseEntity<String> render(
        String component,
        Map<String, Object> props,
        String url,
        InertiaSpringRendererOptions options
    ) {
        return render(getCurrentRequest(), component, props, url, options);
    }

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @param request HTTP request
     * @param url value of the URL field in response
     * @param component name of the component to render in the client
     * @param props regular response data
     * @param options Inertia flags and other Page Object data
     */
    public ResponseEntity<String> render(
        WebRequest request,
        String component,
        Map<String, Object> props,
        String url,
        InertiaSpringRendererOptions options
    ) {
        HttpServletRequest servletRequest = ((ServletRequestAttributes) request).getRequest();

        return render(servletRequest, component, props, url, options);
    }

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @param request HTTP request
     * @param component name of the component to render in the client
     * @param props regular response data
     * @param url value of the URL field in response
     * @param options Inertia flags and other Page Object data
     */
    public ResponseEntity<String> render(
        HttpServletRequest request,
        String component,
        Map<String, Object> props,
        String url,
        InertiaSpringRendererOptions options
    ) {
        return renderer.render(
            new InertiaHttpServletRequest(request),
            options.toCoreRenderingOptions(url, component, props)
        );
    }

    /*
     * Formats the proper redirect response to the specified location.
     *
     * @param location URL to redirect to
     */
    public ResponseEntity<String> redirect(String location) {
        InertiaHttpServletRequest inertiaServletRequest = new InertiaHttpServletRequest(getCurrentRequest());
        return renderer.redirect(inertiaServletRequest, location);
    }

    /*
     * Redirects to an external or non-Inertia URL.
     *
     * @param location external URL to redirect to
     */
    public ResponseEntity<String> location(String url) {
        return renderer.location(url);
    }

    /*
     * Gets the current HTTP request.
     *
     * @returns current request as HttpServletRequest
     */
    private HttpServletRequest getCurrentRequest() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(
            requestAttributes != null,
            "Could not find current request via RequestContextHolder"
        );
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }
}
