package io.gitlab.inertia4j.ktor

import io.gitlab.inertia4j.core.InertiaRenderer
import io.gitlab.inertia4j.core.InertiaRenderingOptions
import io.ktor.server.routing.*
import io.ktor.util.*

class InertiaKtorRenderer internal constructor(
    private val coreRenderer: InertiaRenderer,
    private val configuration: InertiaKtorConfiguration,
) {
    inner class Renderer internal constructor(call: RoutingCall) {
        private val request = InertiaKtorHttpRequest(call.request)
        private val response = InertiaKtorHttpResponse(call)

        /*
         * Renders the Inertia.js formatted response.
         *
         * @param name component name to render in the client
         * @param props data to be provided
         * @param encryptHistory flag to encrypt client history
         * @param clearHistory flag to clear client history
         */
        fun render(
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

            coreRenderer.render(request, response, options)
            response.respond()
        }

        /*
         * Handles internal Inertia redirects.
         *
         * @param location url to be redirected to
         */
        fun redirect(location: String) {
            coreRenderer.redirect(request, response, location)
            response.respond()
        }

        /*
         * Handles non-Inertia redirects and external redirects.
         *
         * @param location url to be redirected to
         */
        fun location(location: String) {
            coreRenderer.location(response, location)
            response.respond()
        }
    }

    companion object {
        val key = AttributeKey<InertiaKtorRenderer>("inertiaKtor")
    }
}
