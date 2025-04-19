package io.github.inertia4j.ktor

import io.ktor.server.routing.*

/**
 * Extension property on [RoutingContext] to easily access the Inertia renderer functions.
 * Provides an instance of [InertiaKtorRenderer.Renderer] bound to the current call context.
 *
 * @throws PluginNotInstalledException if the [Inertia] plugin has not been installed in the application.
 */
val RoutingContext.inertia: InertiaKtorRenderer.Renderer get() {
    return call.application.attributes.getOrNull(InertiaKtorRenderer.key)?.Renderer(call)
        ?: throw PluginNotInstalledException()
}
