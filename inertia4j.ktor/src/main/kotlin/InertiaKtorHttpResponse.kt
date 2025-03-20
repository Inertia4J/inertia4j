package io.gitlab.intertia4j.ktor

import io.gitlab.inertia4j.core.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import kotlinx.coroutines.runBlocking

internal class InertiaKtorHttpResponse(private val call: RoutingCall) : HttpResponse {
    // Ugh... should we refactor core?
    private var code: Int = 200

    override fun setHeader(name: String, value: String) {
        call.response.header(name, value)
    }

    override fun setCode(code: Int) {
        this.code = code
    }

    // TODO: review this use of runBlocking. We may need to change core interfaces to use `Future`s
    override fun writeBody(content: String) = runBlocking {
        call.respond(HttpStatusCode.fromValue(code), content)
    }
}