plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "inertia4j"

include("inertia4j.core")
include("inertia4j.jackson")
include("inertia4j.spring")
include("inertia4j.spring.example")
