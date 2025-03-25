plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.17.2")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
