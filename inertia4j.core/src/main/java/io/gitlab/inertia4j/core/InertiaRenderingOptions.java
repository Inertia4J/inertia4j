package io.gitlab.inertia4j.core;

/**
 * Holds options that will be passed along to the renderer.
 */
public class InertiaRenderingOptions {
    final boolean encryptHistory;
    final boolean clearHistory;
    final String url;
    final String componentName;
    final Object props;

    public InertiaRenderingOptions(
        boolean encryptHistory,
        boolean clearHistory,
        String url,
        String componentName,
        Object props
    ) {
        this.encryptHistory = encryptHistory;
        this.clearHistory = clearHistory;
        this.url = url;
        this.componentName = componentName;
        this.props = props;
    }

    /*
     * Returns a new PageObject with the specified component.
     *
     * @param component name of the component
     * @returns an InertiaRenderingOptions instance
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
