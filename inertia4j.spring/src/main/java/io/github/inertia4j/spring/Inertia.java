package io.github.inertia4j.spring;

import io.github.inertia4j.core.DefaultPageObjectSerializer;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * Static facade providing convenient methods for rendering Inertia responses within Spring controllers.
 * This class simplifies common Inertia operations like rendering components, handling redirects,
 * and managing asset versions. It uses a default setup but can be configured via static setters
 * or Spring Boot properties.
 */
public class Inertia {
    /**
     * Provides the current asset version. Can be replaced with a custom implementation.
     * Defaults to returning null, relying on the core renderer's default if not set.
     */
    public static VersionProvider versionProvider = () -> null;

    private static final InertiaSpringRenderer renderer = new InertiaSpringRenderer(
        new DefaultPageObjectSerializer(),
        versionProvider,
        "templates/app.html"
    );

    private static InertiaSpringRendererOptions defaultOptions = new InertiaSpringRendererOptions();

    /**
     * Sets the {@link VersionProvider} used by the static renderer instance.
     * @param provider The VersionProvider implementation.
     */
    public static void setVersionProvider(VersionProvider provider) {
        versionProvider = provider;
    }

    /**
     * Sets the default value for the `encryptHistory` flag based on the `inertia.history.encrypt` property.
     * @param encryptHistory The default value for encrypting history.
     */
    @Value("${inertia.history.encrypt:false}")
    public static void setHistoryEncryptDefault(boolean encryptHistory) {
        defaultOptions = new InertiaSpringRendererOptions(
            encryptHistory,
            InertiaSpringRendererOptions.defaultClearHistory
        );
    }

    /**
     * Renders an Inertia component with the given properties.
     * Uses the current request URI as the page object URL and default rendering options.
     *
     * @param component The name of the client-side component.
     * @param props     A map of properties to pass to the component.
     * @return A Spring {@link ResponseEntity} containing the Inertia response.
     */
    public static ResponseEntity<String> render(String component, Map<String, Object> props) {
        return render(component, props, getCurrentRequest().getRequestURI());
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
    public static ResponseEntity<String> render(String component, Map<String, Object> props, String url) {
        return render(getCurrentRequest(), component, props, url, defaultOptions);
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
    public static ResponseEntity<String> render(
        String component,
        Map<String, Object> props,
        InertiaSpringRendererOptions options
    ) {
        return render(component, props, getCurrentRequest().getRequestURI(), options);
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
    public static ResponseEntity<String> render(
        String component,
        Map<String, Object> props,
        String url,
        InertiaSpringRendererOptions options
    ) {
        return render(getCurrentRequest(), component, props, url, options);
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
    public static ResponseEntity<String> render(
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
    public static ResponseEntity<String> render(
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
    public static ResponseEntity<String> redirect(String location) {
        InertiaHttpServletRequest inertiaServletRequest = new InertiaHttpServletRequest(getCurrentRequest());
        return renderer.redirect(inertiaServletRequest, location);
    }

    /**
     * Creates an external redirect response using 409 Conflict status code and X-Inertia-Location header.
     *
     * @param url The external URL to redirect to.
     * @return A Spring {@link ResponseEntity} configured for an external Inertia redirect.
     */
    public static ResponseEntity<String> location(String url) {
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

    /**
     * A helper class providing static factory methods for creating {@link InertiaSpringRendererOptions}
     * instances in a more fluent way, primarily for setting history flags.
     */
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
