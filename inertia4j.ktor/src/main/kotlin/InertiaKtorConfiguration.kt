package io.github.inertia4j.ktor

import io.github.inertia4j.core.DefaultPageObjectSerializer
import io.github.inertia4j.core.SimpleTemplateRenderer
import io.github.inertia4j.spi.PageObjectSerializer
import io.github.inertia4j.spi.TemplateRenderer

/**
 * Configuration class for the Inertia Ktor plugin.
 * Allows customization of asset versioning, serialization, template rendering, and history behavior.
 */
class InertiaKtorConfiguration {
    /**
     * Provides the current asset version. Defaults to returning "1".
     * This is used to compare against the `X-Inertia-Version` header in requests.
     */
    var versionProvider: () -> String = { "1" }
    /**
     * The serializer used to convert the [io.github.inertia4j.spi.PageObject] into a JSON string.
     * Defaults to `null`. If left `null`, [DefaultPageObjectSerializer] will be used,
     * which requires Jackson Databind on the classpath.
     * Provide a custom implementation if you don't want to use Jackson or need custom serialization logic.
     */
    var serializer: PageObjectSerializer? = null
    /**
     * The renderer used to render the base HTML template for full page loads.
     * Defaults to `null`. If left `null`, [SimpleTemplateRenderer] will be used,
     * loading the template specified by [templatePath].
     * Provide a custom implementation for integration with other template engines.
     */
    var templateRenderer: TemplateRenderer? = null
    /**
     * The classpath path to the base HTML template file used by [SimpleTemplateRenderer].
     * Defaults to "templates/app.html". Only used if [templateRenderer] is `null`.
     */
    var templatePath: String = "templates/app.html"

    /**
     * Flag to enable or disable history encryption. Defaults to `false`.
     * @see <a href="https://inertiajs.com/history-encryption">Inertia History Encryption</a>
     */
    var encryptHistory: Boolean = false

    internal val templateRendererOrDefault: TemplateRenderer get() {
        return templateRenderer ?: SimpleTemplateRenderer(templatePath)
    }

    internal val serializerOrDefault: PageObjectSerializer get() {
        return serializer ?: DefaultPageObjectSerializer()
    }
}
