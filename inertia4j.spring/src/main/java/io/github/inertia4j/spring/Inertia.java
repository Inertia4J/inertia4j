package io.github.inertia4j.spring;

import io.github.inertia4j.spi.PageObjectSerializer;
import io.github.inertia4j.spi.TemplateRenderer;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.function.Supplier;

/**
 * An injectable Spring bean providing convenient methods for rendering Inertia responses within controllers.
 * This class offers similar functionality to the static {@link Inertia} facade but is designed
 * to be managed by the Spring container, allowing for easier configuration and testing.
 * <p>
 * It requires {@link VersionProvider}, {@link PageObjectSerializer}, and {@link TemplateRenderer}
 * beans to be available in the application context for its construction.
 */
public class Inertia {
    private final InertiaSpringRenderer renderer;
    private static final InertiaSpringRendererOptions defaultOptions = new InertiaSpringRendererOptions();
    private final Supplier<HttpServletRequest> requestSupplier;

    /**
     * Internal constructor used in tests.
     */
    Inertia(
        VersionProvider versionProvider,
        PageObjectSerializer pageObjectSerializer,
        TemplateRenderer templateRenderer,
        Supplier<HttpServletRequest> requestSupplier
    ) {
        this.renderer = new InertiaSpringRenderer(pageObjectSerializer, versionProvider, templateRenderer);
        this.requestSupplier = requestSupplier;
    }

    /**
     * Constructs the Inertia bean with required dependencies.
     * 
     * @param versionProvider      The provider for the current asset version.
     * @param pageObjectSerializer The serializer for the PageObject.
     * @param templateRenderer     The renderer for the base HTML template.
     */
    public Inertia(
        VersionProvider versionProvider,
        PageObjectSerializer pageObjectSerializer,
        TemplateRenderer templateRenderer
    ) {
        this(versionProvider, pageObjectSerializer, templateRenderer, Inertia::getCurrentRequest);
    }

    /**
     * Renders an Inertia component that has no props.
     * Uses the current request URI as the page object URL and default rendering options.
     *
     * @param component The name of the client-side component.
     * @return A Spring {@link ResponseEntity} containing the Inertia response.
     */
    public ResponseEntity<String> render(String component) {
        return render(component, null, requestSupplier.get().getRequestURI());
    }

    /**
     * Renders an Inertia component with the given properties.
     * Uses the current request URI as the page object URL and default rendering options.
     *
     * @param component The name of the client-side component.
     * @param props     A map of properties to pass to the component.
     * @return A Spring {@link ResponseEntity} containing the Inertia response.
     */
    public ResponseEntity<String> render(String component, Map<String, Object> props) {
        return render(component, props, requestSupplier.get().getRequestURI());
    }

    /**
     * Renders an Inertia component with the given properties and a specific URL.
     * Uses default rendering options.
     *
     * @param component The name of the client-side component.
     * @param props     A map of properties to pass to the component.
     * @param url       The URL to be included in the page object.
     * @return A Spring {@link ResponseEntity} containing the Inertia response.
     */
    public ResponseEntity<String> render(String component, Map<String, Object> props, String url) {
        return render(requestSupplier.get(), component, props, url, defaultOptions);
    }

    /**
     * Renders an Inertia component with the given properties and specific rendering options.
     * Uses the current request URI as the page object URL.
     *
     * @param component The name of the client-side component.
     * @param props     A map of properties to pass to the component.
     * @param options   Specific rendering options (e.g., history flags).
     * @return A Spring {@link ResponseEntity} containing the Inertia response.
     */
    public ResponseEntity<String> render(
        String component,
        Map<String, Object> props,
        InertiaSpringRendererOptions options
    ) {
        return render(component, props, requestSupplier.get().getRequestURI(), options);
    }

    /**
     * Renders an Inertia component with the given properties, URL, and specific rendering options.
     * This is the most explicit render method, allowing full control.
     *
     * @param component The name of the client-side component.
     * @param props     A map of properties to pass to the component.
     * @param url       The URL to be included in the page object.
     * @param options   Specific rendering options (e.g., history flags).
     * @return A Spring {@link ResponseEntity} containing the Inertia response.
     */
    public ResponseEntity<String> render(
        String component,
        Map<String, Object> props,
        String url,
        InertiaSpringRendererOptions options
    ) {
        return render(requestSupplier.get(), component, props, url, options);
    }

    /**
     * Renders an Inertia component using an explicit {@link WebRequest}.
     *
     * @param request   The current Spring WebRequest.
     * @param component The name of the client-side component.
     * @param props     A map of properties to pass to the component.
     * @param url       The URL to be included in the page object.
     * @param options   Specific rendering options.
     * @return A Spring {@link ResponseEntity} containing the Inertia response.
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

    /**
     * Renders an Inertia component using an explicit {@link HttpServletRequest}.
     *
     * @param request   The current HttpServletRequest.
     * @param component The name of the client-side component.
     * @param props     A map of properties to pass to the component.
     * @param url       The URL to be included in the page object.
     * @param options   Specific rendering options.
     * @return A Spring {@link ResponseEntity} containing the Inertia response.
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

    /**
     * Creates an Inertia redirect response.
     * Uses a 303 status code for PUT/PATCH/DELETE requests and 302 otherwise.
     *
     * @param location The URL to redirect to.
     * @return A Spring {@link ResponseEntity} configured for an Inertia redirect.
     */
    public ResponseEntity<String> redirect(String location) {
        InertiaHttpServletRequest inertiaServletRequest = new InertiaHttpServletRequest(requestSupplier.get());
        return renderer.redirect(inertiaServletRequest, location);
    }

    /**
     * Creates an external redirect response (using 409 Conflict + X-Inertia-Location header).
     *
     * @param url The external URL to redirect to.
     * @return A Spring {@link ResponseEntity} configured for an external Inertia redirect.
     */
    public ResponseEntity<String> location(String url) {
        return renderer.location(url);
    }

    /**
     * Retrieves the current {@link HttpServletRequest} from the {@link RequestContextHolder}.
     * 
     * @return The current HttpServletRequest.
     * @throws IllegalStateException if the request attributes are not found or not of the expected type.
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

    public static class Options {
        /**
         * Creates options with `clearHistory` set to true and default `encryptHistory`.
         * @return New options instance.
         */
        public static InertiaSpringRendererOptions clearHistory() {
            return new InertiaSpringRendererOptions(InertiaSpringRendererOptions.defaultEncryptHistory, true);
        }

        /**
         * Creates options with the specified `clearHistory` value and default `encryptHistory`.
         * @param clearHistory The value for the clearHistory flag.
         * @return New options instance.
         */
        public static InertiaSpringRendererOptions clearHistory(boolean clearHistory) {
            return new InertiaSpringRendererOptions(InertiaSpringRendererOptions.defaultEncryptHistory, clearHistory);
        }

        /**
         * Creates options with `encryptHistory` set to true and default `clearHistory`.
         * @return New options instance.
         */
        public static InertiaSpringRendererOptions encryptHistory() {
            return new InertiaSpringRendererOptions(true, InertiaSpringRendererOptions.defaultClearHistory);
        }

        /**
         * Creates options with the specified `encryptHistory` value and default `clearHistory`.
         * @param encryptHistory The value for the encryptHistory flag.
         * @return New options instance.
         */
        public static InertiaSpringRendererOptions encryptHistory(boolean encryptHistory) {
            return new InertiaSpringRendererOptions(encryptHistory, InertiaSpringRendererOptions.defaultClearHistory);
        }
    }
}
