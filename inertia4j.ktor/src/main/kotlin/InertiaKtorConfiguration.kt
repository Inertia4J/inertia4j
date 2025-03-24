package io.gitlab.inertia4j.ktor

import io.gitlab.inertia4j.core.PageObjectSerializer
import io.gitlab.inertia4j.jackson.JacksonPageObjectSerializer
import java.util.function.Supplier

// TODO: add config for template formatter
class InertiaKtorConfiguration {
    // TODO: change this to an implementation that throws an error
    var serializer: PageObjectSerializer = JacksonPageObjectSerializer()
    var templatePath: String = "templates/app.html"
    var versionProvider: Supplier<String> = Supplier { "1" }
}
