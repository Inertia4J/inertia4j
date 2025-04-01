package io.gitlab.inertia4j.ktor

import io.gitlab.inertia4j.spi.InertiaException

class PluginNotInstalledException : InertiaException("Inertia plugin was not installed")
