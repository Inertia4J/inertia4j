package io.gitlab.inertia4j.ktor

import io.ktor.server.testing.*
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.get
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InertiaKtorTest {
    @Test
    fun render() = testApplication {
        install(Inertia)

        routing {
            get("/") {
                inertia.render("SampleComponent", "id" to 1, encryptHistory = true, clearHistory = true)
            }
        }

        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)

        val expectedBody = """
            <!doctype html>
            <html lang="en">
            <body>
                <div id="app" data-page='{"component":"SampleComponent","props":{"id":1},"url":"/","version":"1","encryptHistory":true,"clearHistory":true}'></div>
            </body>
            </html>
        """.trimIndent() + '\n'
        assertEquals(expectedBody, response.bodyAsText())
    }
}
