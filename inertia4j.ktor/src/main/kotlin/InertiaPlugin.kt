package io.gitlab.inertia4j.ktor

import io.gitlab.inertia4j.core.InertiaRenderer
import io.ktor.server.application.*

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
