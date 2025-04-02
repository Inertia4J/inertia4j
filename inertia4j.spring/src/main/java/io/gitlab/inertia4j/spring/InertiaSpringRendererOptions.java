package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.InertiaRenderingOptions;

import java.util.Map;

/**
 * Represents rendering options specific to the Spring integration, primarily focusing on
 * history state flags (`encryptHistory`, `clearHistory`).
 * <p>
 * This class exists separately from {@link Inertia.Options} to avoid conflicts with static methods
 * and provide an instance-based way to configure options, often used with the {@link InertiaSpring} bean.
 */
public class InertiaSpringRendererOptions {
    private final boolean encryptHistory;
    private final boolean clearHistory;

    /** Default value for encryptHistory, used by constructors and potentially autoconfiguration. */
    static boolean defaultEncryptHistory = false;
    /** Default value for clearHistory, used by constructors. */
    static final boolean defaultClearHistory = false;

    /**
     * Constructs new options with specified history flags.
     * @param encryptHistory Value for the encryptHistory flag.
     * @param clearHistory   Value for the clearHistory flag.
     */
    public InertiaSpringRendererOptions(boolean encryptHistory, boolean clearHistory) {
        this.encryptHistory = encryptHistory;
        this.clearHistory = clearHistory;
    }

    /** Constructs new options using default history flag values. */
    public InertiaSpringRendererOptions() {
        this(defaultEncryptHistory, defaultClearHistory);
    }

    /**
     * Returns a new options instance with `clearHistory` set to true.
     * @return New options instance.
     */
    public InertiaSpringRendererOptions clearHistory() {
        return new InertiaSpringRendererOptions(encryptHistory, true);
    }

    /**
     * Returns a new options instance with the specified `clearHistory` value.
     * @param clearHistory The value for the clearHistory flag.
     * @return New options instance.
     */
    public InertiaSpringRendererOptions clearHistory(boolean clearHistory) {
        return new InertiaSpringRendererOptions(encryptHistory, clearHistory);
    }

    /**
     * Returns a new options instance with `encryptHistory` set to true.
     * @return New options instance.
     */
    public InertiaSpringRendererOptions encryptHistory() {
        return new InertiaSpringRendererOptions(true, clearHistory);
    }

    /**
     * Returns a new options instance with the specified `encryptHistory` value.
     * @param encryptHistory The value for the encryptHistory flag.
     * @return New options instance.
     */
    public InertiaSpringRendererOptions encryptHistory(boolean encryptHistory) {
        return new InertiaSpringRendererOptions(encryptHistory, clearHistory);
    }

    /**
     * Converts these Spring-specific options into the core {@link InertiaRenderingOptions} object
     * required by the underlying {@link io.gitlab.inertia4j.core.InertiaRenderer}.
     *
     * @param url           The URL for the page object.
     * @param componentName The name of the client-side component.
     * @param props         The properties (data) for the component.
     * @return An instance of {@link InertiaRenderingOptions}.
     */
    InertiaRenderingOptions toCoreRenderingOptions(
        String url,
        String componentName,
        Map<String, Object> props
    ) {
        return new InertiaRenderingOptions(encryptHistory, clearHistory, url, componentName, props);
    }
}
