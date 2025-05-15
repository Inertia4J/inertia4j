plugins {
    kotlin("jvm") version "2.1.20"
    `maven-publish`
    id("signing")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":inertia4j.core"))
    api(project(":inertia4j.spi"))

    // TODO: refactor this to use version catalog
    val ktorVersion = "3.1.1"

    compileOnly("io.ktor:ktor-server-core-jvm:$ktorVersion")
    compileOnly("io.ktor:ktor-server-netty-jvm:$ktorVersion")

    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:2.1.20")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }

    withSourcesJar()
    withJavadocJar()
}

group = "io.github.inertia4j"
version = "1.0.1"

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifactId = "inertia4j-ktor"

            pom {
                name.set("Inertia4J Ktor")
                description.set("Inertia4J back-end adapter for Ktor")
                url.set("https://github.com/Inertia4J/inertia4j")
                inceptionYear.set("2025")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("edrd-f")
                        name.set("Eduardo Fonseca")
                    }
                    developer {
                        id.set("pefcos")
                        name.set("Pedro Fronchetti Costa da Silva")
                        email.set("pfronchetti@gmail.com")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/Inertia4J/inertia4j.git")
                    developerConnection.set("scm:git:ssh://git@github.com:Inertia4J/inertia4j.git")
                    url.set("https://github.com/Inertia4J/inertia4j")
                }
            }
        }
    }

    repositories {
        maven {
            url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI() // used by JReleaser
        }
    }
}

configure<SigningExtension> {
    useGpgCmd()
    if (project.hasProperty("signing.keyId")) {
        useGpgCmd()
    }
}
