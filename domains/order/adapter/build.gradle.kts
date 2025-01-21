plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    // 추가적인 의존성 선언
}