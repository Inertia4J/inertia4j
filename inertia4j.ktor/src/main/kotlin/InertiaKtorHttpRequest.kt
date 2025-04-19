package io.github.inertia4j.ktor

import io.github.inertia4j.core.HttpRequest
import io.ktor.server.request.header
import io.ktor.server.request.httpMethod
import io.ktor.server.request.uri
import io.ktor.server.routing.RoutingRequest

/**
 * Implementation of [HttpRequest] that wraps a Ktor [RoutingRequest].
 * This acts as an adapter between the Ktor request object and the core Inertia4j renderer.
 *
 * @property request The underlying Ktor [RoutingRequest].
 */
internal class InertiaKtorHttpRequest(private val request: RoutingRequest) : HttpRequest {
    /**
     * {@inheritDoc}
     */
    override fun getHeader(name: String): String? = request.header(name)

    /**
     * {@inheritDoc}
     */
    override fun getMethod(): String = request.httpMethod.value

    /**
     * Gets the full request URI as a string.
     */
    val url get() = request.uri
}
