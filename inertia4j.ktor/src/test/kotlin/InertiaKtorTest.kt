package io.gitlab.inertia4j.ktor

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InertiaKtorTest {
    @Test
    fun render() = testApplication {
        install(Inertia) {
            encryptHistory = true
        }

        routing {
            get("/") {
                inertia.render("SampleComponent", "id" to 1, clearHistory = true)
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
