package io.gitlab.inertia4j.ktor

import io.ktor.server.routing.*

val RoutingContext.inertia: io.gitlab.inertia4j.ktor.InertiaKtorRenderer.Renderer get() {
    return call.application.attributes.getOrNull(InertiaKtorRenderer.key)?.Renderer(call)
        ?: throw PluginNotInstalledException()
}
