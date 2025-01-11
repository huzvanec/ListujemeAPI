plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "cz.jeme.programu"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    flatDir {
        dirs("lib/mupdf")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.jetbrains:annotations:26.0.1")
    implementation(files("lib/mupdf/libmupdf.jar"))
}

tasks.jar {
    enabled = false
}