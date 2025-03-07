package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * Bridges Spring-specific rendering with the core InertiaRenderer.
 */
class InertiaSpringRenderer {
    private final InertiaRenderer renderer;
    // Configuration value for encryptHistory flag default
    @Value("${inertia.history.encrypt:false}")
    private boolean inertiaHistoryEncryptDefault = false;

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
        this.renderer.setInertiaHistoryEncryptDefault(inertiaHistoryEncryptDefault);
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
            this.renderer.setInertiaHistoryEncryptDefault(inertiaHistoryEncryptDefault);
        } catch (TemplateRenderingException e) {
            throw new SpringInertiaException(e);
        }
    }

    /*
     * Formats the server response to the Inertia response format.
     * 
     * @param component name of the component to render in the client
     * @param props regular response data
     */
    public <TData> ResponseEntity<String> render(String component, TData props) {
        HttpServletRequest request = getCurrentRequest();
        String url = request.getRequestURI();

        return render(getCurrentRequest()::getHeader, url, component, props);
    }

    /*
     * Formats the server response to the Inertia response format.
     * 
     * @param url value of the URL field in response
     * @param component name of the component to render in the client
     * @param props regular response data
     */
    public <TData> ResponseEntity<String> render(
        String url,
        String component,
        TData props
    ) {
        return render(getCurrentRequest()::getHeader, url, component, props);
    }

    /*
     * Formats the server response to the Inertia response format.
     * 
     * @param WebRequest HTTP request
     * @param url value of the URL field in response
     * @param component name of the component to render in the client
     * @param props regular response data
     */
    public <TData> ResponseEntity<String> render(
        WebRequest request,
        String url,
        String component,
        TData props
    ) {
        return render(request::getHeader, url, component, props);
    }

    /*
     * Sets the encryptHistory flag for the next request.
     *
     * @params encryptHistory flag value
     */
    public void setEncryptHistory(boolean encryptHistory) {
        this.renderer.setEncryptHistory(encryptHistory);
    }

    /*
     * Sets the clearHistory flag for the next request.
     *
     * @params clearHistory flag value
     */
    public void setClearHistory(boolean clearHistory) {
        this.renderer.setClearHistory(clearHistory);
    }

    /*
     * Formats the server response to the Inertia response format.
     * 
     * @param headerGetter request header getter
     * @param url value of the URL field in response
     * @param component name of the component to render in the client
     * @param props regular response data
     */
    private <TData> ResponseEntity<String> render(
        RequestHeaderGetter headerGetter,
        String url,
        String component,
        TData props
    ) {
        SpringHttpResponse inertiaSpringResponse = new SpringHttpResponse();

        try {
            renderer.render(
                headerGetter,
                inertiaSpringResponse,
                url,
                component,
                props
            );
        } catch (SerializationException e) {
            throw new SpringInertiaException(e);
        }

        return inertiaSpringResponse.toResponseEntity();
    }


    /*
     * Gets the current HTTP request.
     * 
     * @param headerGetter request header getter
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
