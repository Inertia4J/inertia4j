package io.gitlab.inertia4j.ktor

import io.gitlab.inertia4j.core.InertiaRenderer
import io.gitlab.inertia4j.core.InertiaRenderingOptions
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.AttributeKey

val Inertia = createApplicationPlugin(
    name = "Inertia",
    createConfiguration = ::InertiaKtorConfiguration
) {
    val coreRenderer = InertiaRenderer(
        pluginConfig.serializer,
        pluginConfig.versionProvider,
        pluginConfig.templatePath
    )
    application.attributes.put(InertiaKtorRenderer.key, InertiaKtorRenderer(coreRenderer))
}
