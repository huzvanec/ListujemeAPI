plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "cz.jeme"
version = "1.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(files("vendor/mupdf/build/java/release/libmupdf.jar"))
    compileOnly(libs.jetbrains.annotations)
}

tasks {
    jar {
        enabled = false
    }
}