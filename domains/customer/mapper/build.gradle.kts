plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    implementation(project(":core"))
    implementation("org.mapstruct:mapstruct")
    kapt("org.mapstruct:mapstruct-processor")
}