package io.gitlab.inertia4j.ktor

import io.gitlab.inertia4j.core.DefaultPageObjectSerializer
import io.gitlab.inertia4j.core.PageObjectSerializer
import io.gitlab.inertia4j.core.SimpleTemplateRenderer
import io.gitlab.inertia4j.core.TemplateRenderer
import java.util.function.Supplier

class InertiaKtorConfiguration {
    var versionProvider: Supplier<String> = Supplier { "1" }
    /*
     * Use null as default to avoid checking for Jackson in case
     * the user provides a custom PageObjectSerializer implementation
     */
    var serializer: PageObjectSerializer? = null
    /*
     * We use null as default because instantiating SimpleTemplateRenderer would
     * capture the value of templatePath and changing it wouldn't take effect
     */
    var templateRenderer: TemplateRenderer? = null
    var templatePath: String = "templates/app.html"

    var encryptHistory: Boolean = false

    internal val templateRendererOrDefault: TemplateRenderer get() {
        return templateRenderer ?: SimpleTemplateRenderer(templatePath)
    }

    internal val serializerOrDefault: PageObjectSerializer get() {
        return serializer ?: DefaultPageObjectSerializer()
    }
}
