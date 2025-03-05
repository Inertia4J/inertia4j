plugins {
    java
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":inertia4j.core"))

    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
}
