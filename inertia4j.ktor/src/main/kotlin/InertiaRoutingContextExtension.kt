package io.gitlab.inertia4j.ktor

import io.ktor.server.routing.*

val RoutingContext.inertia: InertiaKtorRenderer.Renderer get() {
    return call.application.attributes.getOrNull(InertiaKtorRenderer.key)?.Renderer(call)
        ?: throw PluginNotInstalledException()
}
