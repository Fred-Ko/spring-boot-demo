plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    implementation(project(":domains:customer:core"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Kotlin JDSL
    val kotlinJdslVersion = "2.2.1.RELEASE"
    implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter:${kotlinJdslVersion}")

    // MapStruct
    val mapstructVersion = "1.5.5.Final"
    implementation("org.mapstruct:mapstruct:${mapstructVersion}")
    kapt("org.mapstruct:mapstruct-processor:${mapstructVersion}")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

kotlin {
    jvmToolchain(17)
}