plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "inertia4j"

include("inertia4j.spi")
include("inertia4j.core")
include("inertia4j.ktor")
include("inertia4j.spring")
