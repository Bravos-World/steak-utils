plugins {
    java
    id("java-library")
    id("maven-publish")
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.bravos.steak"
version = "1.0.3"
description = "common-utils"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springframework.boot:spring-boot-autoconfigure:4.0.1")
    implementation("org.springframework:spring-context:7.0.2")
    implementation("org.springframework:spring-web:7.0.2")
    implementation("org.springframework:spring-webflux:7.0.2")
    implementation("tools.jackson.core:jackson-databind:3.0.3")
    implementation("tools.jackson.core:jackson-core:3.0.3")
    implementation("org.springframework.kafka:spring-kafka:4.0.1")
    implementation("org.springframework.data:spring-data-redis:4.0.1")
}

publishing {
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}