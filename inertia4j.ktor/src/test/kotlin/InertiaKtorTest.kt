package io.github.inertia4j.ktor

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InertiaKtorTest {
    private fun testApp(block: suspend ApplicationTestBuilder.() -> Unit) = testApplication {
        application {
            install(Inertia) {
                encryptHistory = true
                versionProvider = { "1" }
            }
        }
        block()
    }

    @Test
    fun `render full page`() = testApp {
        routing {
            get("/") {
                inertia.render("SampleComponent", "id" to 1, clearHistory = true)
            }
        }

        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Text.Html, response.contentType())
        assert("X-Inertia" !in response.headers)

        val expectedBody = """
            <!doctype html>
            <html lang="en">
            <body>
                <div id="app" data-page='{&quot;component&quot;:&quot;SampleComponent&quot;,&quot;props&quot;:{&quot;id&quot;:1},&quot;url&quot;:&quot;/&quot;,&quot;version&quot;:&quot;1&quot;,&quot;encryptHistory&quot;:true,&quot;clearHistory&quot;:true}'></div>
            </body>
            </html>
        """.trimIndent()
        assertEquals(expectedBody, response.bodyAsText())
    }

    @Test
    fun `render partial with X-Inertia header`() = testApp {
        routing {
            get("/") {
                inertia.render("SampleComponent", "id" to 1)
            }
        }

        val response = client.get("/") {
            header("X-Inertia", "true")
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Application.Json, response.contentType())
        assertEquals("true", response.headers["X-Inertia"])

        val expectedBody = """{"component":"SampleComponent","props":{"id":1},"url":"/","version":"1","encryptHistory":true,"clearHistory":false}"""
        assertEquals(expectedBody, response.bodyAsText())
    }

    @Test
    fun `render partial with matching X-Inertia and X-Inertia-Version headers`() = testApp {
         routing {
            get("/") {
                inertia.render("SampleComponent", "id" to 1)
            }
        }

        val response = client.get("/") {
            header("X-Inertia", "true")
            header("X-Inertia-Version", "1")
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Application.Json, response.contentType())
        assertEquals("true", response.headers["X-Inertia"])

        val expectedBody = """{"component":"SampleComponent","props":{"id":1},"url":"/","version":"1","encryptHistory":true,"clearHistory":false}"""
        assertEquals(expectedBody, response.bodyAsText())
    }

     @Test
    fun `render conflict with mismatching X-Inertia and X-Inertia-Version headers`() = testApp {
         routing {
            get("/") {
                inertia.render("SampleComponent", "id" to 1)
            }
        }

        val response = client.get("/") {
            header("X-Inertia", "true")
            header("X-Inertia-Version", "stale-version")
        }
        assertEquals(HttpStatusCode.Conflict, response.status)
        assert("X-Inertia" !in response.headers)
        assert(response.bodyAsText().isEmpty())
    }

    @Test
    fun `redirect internal with X-Inertia header`() = testApp {
        // Disable followRedirects to capture the 302
        val client = createClient { followRedirects = false }
        routing {
            put("/redirect") {
                inertia.redirect("/target")
            }
        }

        val response = client.put("/redirect") {
            header("X-Inertia", "true")
        }
        assertEquals(HttpStatusCode.SeeOther, response.status)
        assert("X-Inertia" !in response.headers)
        assertEquals("/target", response.headers[HttpHeaders.Location])
        assert(response.bodyAsText().isEmpty())
    }

    @Test
    fun `redirect internal without X-Inertia header`() = testApp {
        // Disable followRedirects to capture the 302
        val client = createClient { followRedirects = false }
        routing {
            get("/redirect") {
                inertia.redirect("/target")
            }
        }

        val response = client.get("/redirect")
        assertEquals(HttpStatusCode.Found, response.status)
        assert("X-Inertia" !in response.headers)
        assertEquals("/target", response.headers[HttpHeaders.Location])
        assert(response.bodyAsText().isEmpty())
    }

    @Test
    fun `location external redirect with X-Inertia header`() = testApp {
         routing {
            get("/external") {
                inertia.location("https://external.example.com")
            }
        }

        val response = client.get("/external") {
            header("X-Inertia", "true")
        }
        assertEquals(HttpStatusCode.Conflict, response.status)
        assert("X-Inertia" !in response.headers)
        assertEquals("https://external.example.com", response.headers["X-Inertia-Location"])
        assert(response.bodyAsText().isEmpty())
    }
}
