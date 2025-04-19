package io.github.inertia4j.core;

import java.util.Map;

/**
 * Holds options that will be passed along to the renderer.
 */
public class InertiaRenderingOptions {
    final boolean encryptHistory;
    final boolean clearHistory;
    final String url;
    final String componentName;
    final Map<String, Object> props;

    /**
     * Constructs a new set of rendering options.
     *
     * @param encryptHistory Whether to encrypt the browser history state.
     * @param clearHistory   Whether to clear the browser history state.
     * @param url            The URL for the page object.
     * @param componentName  The name of the client-side component to render.
     * @param props          The properties (data) to pass to the component.
     */
    public InertiaRenderingOptions(
        boolean encryptHistory,
        boolean clearHistory,
        String url,
        String componentName,
        Map<String, Object> props
    ) {
        this.encryptHistory = encryptHistory;
        this.clearHistory = clearHistory;
        this.url = url;
        this.componentName = componentName;
        this.props = props;
    }

    /**
     * Creates a new {@code InertiaRenderingOptions} instance with a potentially different component name.
     * This is used internally when handling partial reloads requested via the `X-Inertia-Partial-Component` header,
     * allowing the server to respond with the same props but target a different component on the client-side.
     *
     * @param component The name of the component specified in the partial reload request.
     * @return A new {@code InertiaRenderingOptions} instance with the updated component name.
     */
    public InertiaRenderingOptions withPartialComponent(String component) {
        return new InertiaRenderingOptions(
            this.encryptHistory,
            this.clearHistory,
            this.url,
            component,
            this.props
        );
    }
}
