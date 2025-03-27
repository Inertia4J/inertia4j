package io.gitlab.inertia4j.ktor

import io.gitlab.inertia4j.core.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import kotlinx.coroutines.runBlocking

internal class InertiaKtorHttpResponse(private val call: RoutingCall) : HttpResponse {
    private var code: Int = 200
    private var body: String = ""

    override fun setHeader(name: String, value: String): InertiaKtorHttpResponse {
        call.response.header(name, value)
        return this
    }

    override fun setCode(code: Int): InertiaKtorHttpResponse {
        this.code = code
        return this
    }

    override fun writeBody(content: String): InertiaKtorHttpResponse {
        this.body = content
        return this
    }

    fun respond() = runBlocking {
        call.respond(HttpStatusCode.fromValue(code), body)
    }
}