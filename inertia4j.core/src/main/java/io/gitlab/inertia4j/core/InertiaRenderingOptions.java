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
    final Object version;

    public InertiaRenderingOptions(
        boolean encryptHistory,
        boolean clearHistory,
        String url,
        String componentName,
        Object props,
        Object version
    ) {
        this.encryptHistory = encryptHistory;
        this.clearHistory = clearHistory;
        this.url = url;
        this.componentName = componentName;
        this.props = props;
        this.version = version;
    }
}
