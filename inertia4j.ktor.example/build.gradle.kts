plugins {
    kotlin("jvm") version "2.1.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":inertia4j.ktor"))

    // TODO: refactor this to use version catalog
    val ktorVersion = "3.1.1"

    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")

    implementation("ch.qos.logback:logback-classic:1.5.18")
}
