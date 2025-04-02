package io.gitlab.inertia4j.ktor

import io.gitlab.inertia4j.core.InertiaRenderer
import io.ktor.server.application.*

/**
 * The main Ktor Application Plugin for integrating Inertia4j.
 * This plugin initializes the core [InertiaRenderer] based on the provided [InertiaKtorConfiguration]
 * and makes the Ktor-specific [InertiaKtorRenderer] available via application attributes.
 */
val Inertia = createApplicationPlugin(
    name = "Inertia",
    createConfiguration = ::InertiaKtorConfiguration
) {
    val coreRenderer = InertiaRenderer(
        pluginConfig.serializerOrDefault,
        pluginConfig.versionProvider,
        pluginConfig.templateRendererOrDefault
    )
    application.attributes.put(
        InertiaKtorRenderer.key,
        InertiaKtorRenderer(coreRenderer, pluginConfig)
    )
}
