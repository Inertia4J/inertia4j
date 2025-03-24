plugins {
    kotlin("jvm") version "2.1.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":inertia4j.core"))
    implementation(project(":inertia4j.jackson"))

    // TODO: refactor this to use version catalog
    val ktorVersion = "3.1.1"

    // TODO: change all non-test deps to compileOnly
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")

    implementation("ch.qos.logback:logback-classic:1.5.18")

    testImplementation("io.ktor:ktor-server-test-host-jvm:$ktorVersion")
}
