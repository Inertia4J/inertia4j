package io.gitlab.intertia4j.ktor

import io.gitlab.inertia4j.core.InertiaRenderer
import io.gitlab.inertia4j.core.InertiaRenderingOptions
import io.gitlab.inertia4j.core.PageObjectSerializer
import io.gitlab.inertia4j.jackson.JacksonPageObjectSerializer
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.util.AttributeKey
import java.util.function.Supplier

// TODO: add config for template formatter
class InertiaKtorConfiguration {
    // TODO: change this to an implementation that throws an error
    var serializer: PageObjectSerializer = JacksonPageObjectSerializer()
    var templatePath: String = "templates/app.html"
    var versionProvider: Supplier<String> = Supplier { "1" }
}

class InertiaKtor internal constructor(private val coreRenderer: InertiaRenderer) {
    inner class Renderer internal constructor(call: RoutingCall) {
        private val request = InertiaKtorHttpRequest(call.request)
        private val response = InertiaKtorHttpResponse(call)

        // TODO: add all args to `render` and build options accordingly
        fun render(name: String, vararg props: Pair<String, Any>) {
            val options = InertiaRenderingOptions(false, false, "/", name, mapOf(*props))

            coreRenderer.render(request, response, options)
        }
    }

    companion object {
        val key = AttributeKey<InertiaKtor>("inertiaKtor")
    }
}

class PluginNotInstalledException : Exception("Inertia plugin was not installed")

val RoutingContext.inertia: InertiaKtor.Renderer get() {
    return call.application.attributes.getOrNull(InertiaKtor.key)?.Renderer(call)
        ?: throw PluginNotInstalledException()
}

val Inertia = createApplicationPlugin(
    name = "Inertia",
    createConfiguration = ::InertiaKtorConfiguration
) {
    val coreRenderer = InertiaRenderer(
        pluginConfig.serializer,
        pluginConfig.versionProvider,
        pluginConfig.templatePath
    )
    application.attributes.put(InertiaKtor.key, InertiaKtor(coreRenderer))
}

fun main() {
    embeddedServer(Netty) {
        install(Inertia) {
            serializer = JacksonPageObjectSerializer()
        }

        routing {
            get("/") {
                val records = listOf(Record(1, "Meddle"))

                inertia.render("records/Index", "records" to records)
            }
        }
    }
}

data class Record(val id: Int, val name: String)
