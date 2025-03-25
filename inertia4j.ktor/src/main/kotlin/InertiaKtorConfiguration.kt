package io.gitlab.inertia4j.ktor

import io.gitlab.inertia4j.core.PageObjectSerializer
import io.gitlab.inertia4j.core.SimpleTemplateRenderer
import io.gitlab.inertia4j.core.TemplateRenderer
import io.gitlab.inertia4j.jackson.JacksonPageObjectSerializer
import java.util.function.Supplier

class InertiaKtorConfiguration {
    // TODO: change this to an implementation that throws an error
    var serializer: PageObjectSerializer = JacksonPageObjectSerializer()
    var templatePath: String = "templates/app.html"
    /*
     * We use null as default because instantiating SimpleTemplateRenderer would
     * capture the value of templatePath and changing it wouldn't take effect
     */
    var templateRenderer: TemplateRenderer? = null
    var versionProvider: Supplier<String> = Supplier { "1" }
    var encryptHistory: Boolean = false

    internal val templateRendererOrDefault: TemplateRenderer get() {
        return templateRenderer ?: SimpleTemplateRenderer(templatePath)
    }
}
