package io.gitlab.intertia4j.ktor

import com.google.gson.Gson
import io.gitlab.inertia4j.ktor.Inertia
import io.gitlab.inertia4j.ktor.inertia
import io.gitlab.inertia4j.spi.PageObject
import io.gitlab.inertia4j.spi.PageObjectSerializer
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*

private object CustomPageObjectSerializer : PageObjectSerializer {
    override fun serialize(
        pageObject: PageObject,
        partialDataProps: List<String>?,
    ): String {
        return Gson().toJson(pageObject)
    }
}

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(Inertia) {
            serializer = CustomPageObjectSerializer
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
