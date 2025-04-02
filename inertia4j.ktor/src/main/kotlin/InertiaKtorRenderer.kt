package io.gitlab.inertia4j.ktor

import io.gitlab.inertia4j.core.HttpResponse
import io.gitlab.inertia4j.core.InertiaRenderer
import io.gitlab.inertia4j.core.InertiaRenderingOptions
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

/**
 * Ktor-specific renderer that integrates with the core [InertiaRenderer].
 * Provides an inner [Renderer] class accessible within Ktor routing handlers
 * to render Inertia responses or handle redirects.
 *
 * @property coreRenderer The underlying core Inertia renderer instance.
 * @property configuration The Ktor plugin configuration.
 */
class InertiaKtorRenderer internal constructor(
    private val coreRenderer: InertiaRenderer,
    private val configuration: InertiaKtorConfiguration,
) {
    /**
     * Provides rendering functions within the context of a specific Ktor [RoutingCall].
     * Instances of this class are typically obtained via `call.inertia`.
     *
     * @property call The current Ktor [RoutingCall].
     */
    inner class Renderer internal constructor(private val call: RoutingCall) {
        private val request = InertiaKtorHttpRequest(call.request)

        /**
         * Renders an Inertia response, either as a full HTML page or a JSON response depending on the request headers.
         *
         * @param name The name of the client-side component to render.
         * @param props Key-value pairs representing the properties (data) to pass to the component.
         * @param url The URL to be included in the page object (defaults to the current request URI).
         * @param encryptHistory Whether to encrypt the browser history state for this response (defaults to configuration setting).
         * @param clearHistory Whether to clear the browser history state for this response (defaults to false).
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

        /**
         * Performs an Inertia redirect. Uses a 303 status code for PUT/PATCH/DELETE requests and 302 otherwise.
         *
         * @param location The URL to redirect to.
         */
        suspend fun redirect(location: String) {
            respond(coreRenderer.redirect(request, location))
        }

        /**
         * Performs an external redirect by sending a 409 Conflict response with the `X-Inertia-Location` header.
         * This instructs the client-side adapter to perform a hard browser visit.
         *
         * @param location The external URL to redirect to.
         */
        suspend fun location(location: String) {
            respond(coreRenderer.location(location))
        }

        private suspend fun respond(coreResponse: HttpResponse) {
            coreResponse.headers.forEach { (name: String, values: List<String>) ->
                values.forEach { call.response.header(name, it) }
            }
            call.respond(HttpStatusCode.fromValue(coreResponse.code), coreResponse.body ?: "")
        }
    }

    companion object {
        /**
         * The attribute key used to store and retrieve the [InertiaKtorRenderer] instance within Ktor attributes.
         */
        val key = AttributeKey<InertiaKtorRenderer>("inertiaKtor")
    }
}
