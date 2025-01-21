plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    // Spring
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.data:spring-data-commons")

    // Validation
    implementation("jakarta.validation:jakarta.validation-api")

    // ANTLR
    implementation("org.antlr:antlr4-runtime:4.13.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}