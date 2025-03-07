package io.gitlab.inertia4j.core;

/**
 * Holds options that will be passed along to the renderer.
 */
public class InertiaRenderingOptions {
    final boolean encryptHistory;
    final boolean clearHistory;

    public InertiaRenderingOptions(boolean encryptHistory, boolean clearHistory) {
        this.encryptHistory = encryptHistory;
        this.clearHistory = clearHistory;
    }
}
