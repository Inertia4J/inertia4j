package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.jackson.JacksonPageObjectSerializer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

/*
 * Implements a Facade for the InertiaRenderer in order to simplify Spring controller logic.
 */
public class Inertia {
    private static final InertiaSpringRenderer renderer = new InertiaSpringRenderer(
        new JacksonPageObjectSerializer(),
        "templates/app.html"
    );

    private static InertiaSpringRendererOptions defaultOptions = new InertiaSpringRendererOptions();

    @Value("${inertia.history.encrypt:false}")
    public static void setHistoryEncryptDefault(boolean encryptHistory) {
        defaultOptions = new InertiaSpringRendererOptions(
            encryptHistory,
            InertiaSpringRendererOptions.defaultClearHistory
        );
    }

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @params component name of the component to render in the client
     * @param props regular response data
     * @param options rendering options
     */
    public static ResponseEntity<String> render(String component, Object props) {
        return render(getCurrentRequest().getRequestURI(), component, props);
    }

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @param url value of the URL field in response
     * @params component name of the component to render in the client
     * @param props regular response data
     */
    public static ResponseEntity<String> render(String url, String component, Object props) {
        return render(getCurrentRequest(), url, component, props, defaultOptions);
    }

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @params component name of the component to render in the client
     * @param props regular response data
     * @param options rendering options
     */
    public static ResponseEntity<String> render(String component, Object props, InertiaSpringRendererOptions options) {
        return render(getCurrentRequest().getRequestURI(), component, props, options);
    }

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @param url value of the URL field in response
     * @params component name of the component to render in the client
     * @param props regular response data
     * @param options rendering options
     */
    public static ResponseEntity<String> render(String url, String component, Object props, InertiaSpringRendererOptions options) {
        return render(getCurrentRequest(), url, component, props, options);
    }

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @param request HTTP request
     * @param url value of the URL field in response
     * @param component name of the component to render in the client
     * @param props regular response data
     */
    public static ResponseEntity<String> render(
        WebRequest request,
        String url,
        String component,
        Object props,
        InertiaSpringRendererOptions options
    ) {
        return renderer.render(request::getHeader, url, component, props, options.toCoreRenderingOptions());
    }

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @param request HTTP request
     * @param url value of the URL field in response
     * @param component name of the component to render in the client
     * @param props regular response data
     */
    public static ResponseEntity<String> render(
        HttpServletRequest request,
        String url,
        String component,
        Object props,
        InertiaSpringRendererOptions options
    ) {
        return renderer.render(request::getHeader, url, component, props, options.toCoreRenderingOptions());
    }

    /*
     * Gets the current HTTP request.
     *
     * @param headerGetter request header getter
     * @returns current request as HttpServletRequest
     */
    private static HttpServletRequest getCurrentRequest() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(
            requestAttributes != null,
            "Could not find current request via RequestContextHolder"
        );
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * A builder class to make option passing more idiomatic
     */
    public static class Options {
        public static InertiaSpringRendererOptions clearHistory() {
            return new InertiaSpringRendererOptions(InertiaSpringRendererOptions.defaultEncryptHistory, true);
        }

        public static InertiaSpringRendererOptions clearHistory(boolean clearHistory) {
            return new InertiaSpringRendererOptions(InertiaSpringRendererOptions.defaultEncryptHistory, clearHistory);
        }

        public static InertiaSpringRendererOptions encryptHistory() {
            return new InertiaSpringRendererOptions(true, InertiaSpringRendererOptions.defaultClearHistory);
        }

        public static InertiaSpringRendererOptions encryptHistory(boolean encryptHistory) {
            return new InertiaSpringRendererOptions(encryptHistory, InertiaSpringRendererOptions.defaultClearHistory);
        }
    }
}
