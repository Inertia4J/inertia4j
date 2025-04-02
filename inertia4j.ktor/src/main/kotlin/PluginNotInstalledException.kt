package io.gitlab.inertia4j.ktor

import io.gitlab.inertia4j.spi.InertiaException

/**
 * Exception thrown when attempting to use the `inertia` extension property on [io.ktor.server.routing.RoutingContext]
 * before the [Inertia] plugin has been installed in the Ktor application.
 */
class PluginNotInstalledException : InertiaException("Inertia plugin was not installed")
