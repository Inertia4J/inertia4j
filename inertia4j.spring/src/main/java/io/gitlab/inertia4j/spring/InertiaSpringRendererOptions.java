package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.InertiaRenderingOptions;

/**
 * Builder class to make option passing more idiomatic. Necessary to separate
 * from Inertia.Options because of conflicts with its static methods.
 */
public class InertiaSpringRendererOptions {
    private final boolean encryptHistory;
    private final boolean clearHistory;

    static boolean defaultEncryptHistory = false;
    static final boolean defaultClearHistory = false;

    public InertiaSpringRendererOptions(boolean encryptHistory, boolean clearHistory) {
        this.encryptHistory = encryptHistory;
        this.clearHistory = clearHistory;
    }

    public InertiaSpringRendererOptions() {
        this(defaultEncryptHistory, defaultClearHistory);
    }

    public InertiaSpringRendererOptions clearHistory() {
        return new InertiaSpringRendererOptions(encryptHistory, true);
    }

    public InertiaSpringRendererOptions clearHistory(boolean clearHistory) {
        return new InertiaSpringRendererOptions(encryptHistory, clearHistory);
    }

    public InertiaSpringRendererOptions encryptHistory() {
        return new InertiaSpringRendererOptions(true, clearHistory);
    }

    public InertiaSpringRendererOptions encryptHistory(boolean encryptHistory) {
        return new InertiaSpringRendererOptions(encryptHistory, clearHistory);
    }

    /*
     * We must convert the options to the core class since the
     * core API is not exposed as a dependency to Spring projects
     */
    InertiaRenderingOptions toCoreRenderingOptions(
        String url,
        String componentName,
        Object props,
        Object version
    ) {
        return new InertiaRenderingOptions(encryptHistory, clearHistory, url, componentName, props, version);
    }
}
