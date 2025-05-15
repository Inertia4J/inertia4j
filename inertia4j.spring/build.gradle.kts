plugins {
    `java-library`
    id("org.springframework.boot") version "2.7.18" apply false
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    `maven-publish`
    id("signing")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }

    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":inertia4j.core"))
    api(project(":inertia4j.spi"))

    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
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
version = "1.0.1"

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifactId = "inertia4j-spring"

            pom {
                name.set("Inertia4J Spring")
                description.set("Inertia4J back-end adapter for Spring")
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

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}
