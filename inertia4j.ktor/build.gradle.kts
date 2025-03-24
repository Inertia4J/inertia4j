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

    compileOnly("io.ktor:ktor-server-core-jvm:$ktorVersion")
    compileOnly("io.ktor:ktor-server-netty-jvm:$ktorVersion")

    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:2.1.20")
}

tasks.test {
    useJUnitPlatform()
}
