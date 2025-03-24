package io.gitlab.intertia4j.ktor

import io.gitlab.inertia4j.ktor.Inertia
import io.gitlab.inertia4j.ktor.inertia
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(Inertia) {
            // TODO: move PageObjectSerializer and TemplateRenderer interfaces to
            //  a config module and expose it as an API on framework adapter modules
            // serializer = JacksonPageObjectSerializer()
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
    }.start(wait = true)
}

data class Record(val id: Int, val name: String)
