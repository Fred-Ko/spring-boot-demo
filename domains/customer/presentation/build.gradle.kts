// 자동 생성된 build.gradle.kts
plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.spring") version "2.1.0"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(project(":domains:customer:domain"))

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

