plugins {
    `java-library`
    `maven-publish`
    id("signing")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.inertia4j:inertia4j-spi:1.0.1")

    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.17.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.12.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("com.fasterxml.jackson.core:jackson-databind:2.17.2")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }

    withSourcesJar()
    withJavadocJar()
}

tasks.test {
    useJUnitPlatform()
}

group = "io.github.inertia4j"
version = "1.0.2"

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifactId = "inertia4j-core"

            pom {
                name.set("Inertia4J Core")
                description.set("Inertia4J back-end adapter core")
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
