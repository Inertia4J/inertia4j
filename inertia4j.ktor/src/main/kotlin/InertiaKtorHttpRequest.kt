package io.gitlab.inertia4j.ktor

import io.gitlab.inertia4j.core.HttpRequest
import io.ktor.server.request.header
import io.ktor.server.request.httpMethod
import io.ktor.server.routing.RoutingRequest

internal class InertiaKtorHttpRequest(private val request: RoutingRequest) : HttpRequest {
    override fun getHeader(name: String): String? = request.header(name)

    override fun getMethod(): String = request.httpMethod.value
}
