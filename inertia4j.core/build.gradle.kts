plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":inertia4j.spi"))

    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.17.2")

    testCompileOnly("org.junit:junit-jupiter-api:5.12.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.test {
    useJUnitPlatform()
}
