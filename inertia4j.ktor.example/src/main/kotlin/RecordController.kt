package io.gitlab.intertia4j.ktor

import io.gitlab.inertia4j.core.InertiaRenderer
import io.gitlab.inertia4j.core.InertiaRenderingOptions
import io.gitlab.inertia4j.core.PageObjectSerializer
import io.gitlab.inertia4j.jackson.JacksonPageObjectSerializer
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.AttributeKey
import java.util.function.Supplier

fun main() {
    embeddedServer(Netty) {
        install(Inertia) {
            serializer = JacksonPageObjectSerializer()
        }

        routing {
            get("/") {
                val records = listOf(Record(1, "Meddle"))

                inertia.render("records/Index", "records" to records)
            }

            get("/redirect") {
                inertia.redirect("/")
            }
        }
    }
}

data class Record(val id: Int, val name: String)
