plugins {
    `java-library`
    id("org.springframework.boot") version "3.3.12" apply false
    id("io.spring.dependency-management") version "1.1.7"
    `maven-publish`
    id("signing")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }

    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":inertia4j.core"))
    api(project(":inertia4j.spi"))

    compileOnly("jakarta.servlet:jakarta.servlet-api")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("org.springframework:spring-web")

    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

group = "io.github.inertia4j"
version = "1.0.4"

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifactId = "inertia4j-spring-shared"

            pom {
                name.set("Inertia4J Shared")
                description.set("Inertia4J shared logic with Spring Boot 3 and Spring Boot 4 adapters")
                url.set("https://github.com/Inertia4J/inertia4j")
                inceptionYear.set("2026")

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
            url = layout.buildDirectory.dir("deploy").get().asFile.toURI()
        }
    }
}

configure<SigningExtension> {
    useGpgCmd()
    if (project.hasProperty("signing.keyId")) {
        useGpgCmd()
    }
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}
