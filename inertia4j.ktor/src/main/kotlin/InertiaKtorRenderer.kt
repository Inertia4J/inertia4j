package io.gitlab.inertia4j.ktor

import io.gitlab.inertia4j.core.HttpResponse
import io.gitlab.inertia4j.core.InertiaRenderer
import io.gitlab.inertia4j.core.InertiaRenderingOptions
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.util.*

class InertiaKtorRenderer internal constructor(
    private val coreRenderer: InertiaRenderer,
    private val configuration: InertiaKtorConfiguration,
) {
    inner class Renderer internal constructor(private val call: RoutingCall) {
        private val request = InertiaKtorHttpRequest(call.request)

        /*
         * Renders the Inertia.js formatted response.
         *
         * @param name component name to render in the client
         * @param props data to be provided
         * @param encryptHistory flag to encrypt client history
         * @param clearHistory flag to clear client history
         */
        suspend fun render(
            name: String,
            vararg props: Pair<String, Any>,
            url: String = request.url,
            encryptHistory: Boolean = configuration.encryptHistory,
            clearHistory: Boolean = false
        ) {
            val options = InertiaRenderingOptions(
                encryptHistory,
                clearHistory,
                url,
                name,
                mapOf(*props)
            )
            respond(coreRenderer.render(request, options))
        }

        /*
         * Handles internal Inertia redirects.
         *
         * @param location url to be redirected to
         */
        suspend fun redirect(location: String) {
            respond(coreRenderer.redirect(request, location))
        }

        /*
         * Handles non-Inertia redirects and external redirects.
         *
         * @param location url to be redirected to
         */
        suspend fun location(location: String) {
            respond(coreRenderer.location(location))
        }

        private suspend fun respond(coreResponse: HttpResponse) {
            coreResponse.headers.forEach { (name: String, values: List<String>) ->
                values.forEach { call.response.header(name, it) }
            }
            call.respond(HttpStatusCode.fromValue(coreResponse.code), coreResponse.body)
        }
    }

    companion object {
        val key = AttributeKey<InertiaKtorRenderer>("inertiaKtor")
    }
}
