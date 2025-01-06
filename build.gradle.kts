import java.io.File

// 버전 변수 선언
val kotlinVersion = "2.1.0"
val springBootVersion = "3.4.1"
val dependencyManagementVersion = "1.1.7"

plugins {
    // 필요한 플러그인들을 루트 프로젝트에 적용
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.spring") version "2.1.0"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

allprojects {
    group = "com.restaurant"
    version = "3.4.1"

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

subprojects{
    apply(plugin = "kotlin")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
    }
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/main/kotlin"))
        }
    }
}


// ===============================================================================================================================

group = "com.restaurant" // 슬래시 대신 점 사용

tasks.register("createMicroserviceModule") {
    group = "setup"
    description = "Creates the folder structure and essential files for a specified service with CQRS and Hexagonal Architecture"

    // 서비스 이름과 베이스 디렉토리를 필수 인자로 받습니다.
    val baseDirPath: String? = project.findProperty("baseDir") as String?
    val serviceName: String? = project.findProperty("serviceName") as String?

    doLast {
        if (baseDirPath == null) {
            println("Error: baseDir is not set")
            throw GradleException("baseDir is not set")
        }

        if (serviceName == null) {
            println("Error: serviceName is not set")
            throw GradleException("serviceName is not set")
        }

        val baseDir = file("$baseDirPath/$serviceName")

        if (!baseDir.exists()) {
            baseDir.mkdirs()
            println("Created base directory: $baseDir")
        } else {
            println("Base directory already exists: $baseDir")
        }

        // Define the list of directories to create
        val directories = listOf(
            "core/src/main/kotlin/com/restaurant/$serviceName/core/application/command/usecase",
            "core/src/main/kotlin/com/restaurant/$serviceName/core/application/command/handlers",
            "core/src/main/kotlin/com/restaurant/$serviceName/core/application/query/usecase",
            "core/src/main/kotlin/com/restaurant/$serviceName/core/application/query/handlers",
            "core/src/main/kotlin/com/restaurant/$serviceName/core/application/service",
            "core/src/main/kotlin/com/restaurant/$serviceName/core/domain/model",
            "core/src/main/kotlin/com/restaurant/$serviceName/core/domain/repository",
            "core/src/main/resources",
            "core/src/test/kotlin/com/restaurant/$serviceName/core",
            
            "adapter-inbound/src/main/kotlin/com/restaurant/$serviceName/adapter/inbound/controller",
            "adapter-inbound/src/main/resources",
            "adapter-inbound/src/test/kotlin/com/restaurant/$serviceName/adapter/inbound",
            "adapter-inbound/src/test/resources",
            
            "adapter-outbound/src/main/kotlin/com/restaurant/$serviceName/adapter/outbound/api",
            "adapter-outbound/src/main/kotlin/com/restaurant/$serviceName/adapter/outbound/persistence/entity",
            "adapter-outbound/src/main/kotlin/com/restaurant/$serviceName/adapter/outbound/persistence/repository",
            "adapter-outbound/src/main/resources",
            "adapter-outbound/src/test/kotlin/com/restaurant/$serviceName/adapter/outbound/api",
            "adapter-outbound/src/test/kotlin/com/restaurant/$serviceName/adapter/outbound/persistence/entity",
            "adapter-outbound/src/test/kotlin/com/restaurant/$serviceName/adapter/outbound/persistence/repository",
            "adapter-outbound/src/test/resources",
            
            "mapper/src/main/kotlin/com/restaurant/$serviceName/mapper",
            "mapper/src/test/kotlin/com/restaurant/$serviceName/mapper",
            
            "shared/src/main/kotlin/com/restaurant/$serviceName/shared/config",
            "shared/src/main/kotlin/com/restaurant/$serviceName/shared/dto",
            "shared/src/main/kotlin/com/restaurant/$serviceName/shared/util",
            "shared/src/main/resources",
            "shared/src/test/kotlin/com/restaurant/$serviceName/shared/config",
            "shared/src/test/kotlin/com/restaurant/$serviceName/shared/dto",
            "shared/src/test/kotlin/com/restaurant/$serviceName/shared/util",
            "shared/src/test/resources",
            
            "test/src"
        )

        // Create each directory if it doesn't exist
        directories.forEach { dirPath ->
            val dir = file("$baseDir/$dirPath")
            if (!dir.exists()) {
                dir.mkdirs()
                println("Created directory: $dir")
            } else {
                println("Directory already exists: $dir")
            }
        }

        // Define the list of essential files to create
        val files = listOf(
            // Root of service
            "$baseDir/build.gradle.kts",

            // Core module
            "$baseDir/core/build.gradle.kts",

            // Adapter-Inbound module
            "$baseDir/adapter-inbound/build.gradle.kts",

            // Adapter-Outbound module
            "$baseDir/adapter-outbound/build.gradle.kts",

            // Mapper module
            "$baseDir/mapper/build.gradle.kts",

            // Shared module
            "$baseDir/shared/build.gradle.kts",

            // 통합 테스트 모듈
            "$baseDir/test/build.gradle.kts",

            // Core application.yml
            "$baseDir/core/src/main/resources/application.yml",

            // Adapter-Inbound application.yml
            "$baseDir/adapter-inbound/src/main/resources/application.yml",

            // Adapter-Outbound application.yml
            "$baseDir/adapter-outbound/src/main/resources/application.yml",

            // Shared application.yml
            "$baseDir/shared/src/main/resources/application.yml"
        )

        // Create each file with template content if it doesn't exist
        files.forEach { filePath ->
            val file = file(filePath)
            if (!file.exists()) {
                file.parentFile.mkdirs() // Ensure parent directories exist
                file.createNewFile()
                // Write template content based on the file type
                when {
                    filePath.endsWith("build.gradle.kts") -> {
                        val moduleName = filePath.substringAfter("$serviceName/").substringBefore("/")
                        val content = when (moduleName) {
                            "core" -> """
                                plugins {
                                    kotlin("jvm")
                                }

                                dependencies {
                                    implementation(project(":common-shared"))
                                }
                                """.trimIndent()
                            "adapter-inbound" -> """
                                plugins {
                                    kotlin("jvm")
                                }

                                dependencies {
                                    implementation(project(":core"))
                                    implementation("org.springframework.boot:spring-boot-starter-web")
                                    // 추가적인 의존성 선언
                                }
                                """.trimIndent()
                            "adapter-outbound" -> """
                                plugins {
                                    kotlin("jvm")
                                }

                                dependencies {
                                    implementation(project(":core"))
                                    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
                                    implementation("org.postgresql:postgresql")
                                    // 추가적인 의존성 선언
                                }
                                """.trimIndent()
                            "mapper" -> """
                                plugins {
                                    kotlin("jvm")
                                    kotlin("kapt")
                                }

                                dependencies {
                                    implementation(project(":core"))
                                    implementation("org.mapstruct:mapstruct")
                                    kapt("org.mapstruct:mapstruct-processor")
                                }
                                """.trimIndent()
                            "shared" -> """
                                plugins {
                                    kotlin("jvm")
                                }

                                dependencies {
                                    implementation(project(":common-shared"))
                                    // 추가적인 의존성 선언
                                }
                                """.trimIndent()
                            "test" -> """
                                plugins {
                                    kotlin("jvm")
                                }

                                dependencies {
                                    implementation(project(":core"))
                                    implementation("org.springframework.boot:spring-boot-starter-test")
                                    // 추가적인 의존성 선언
                                }
                                """.trimIndent()
                            else -> """
                                plugins {
                                    kotlin("jvm")
                                }

                                dependencies {
                                    // 모듈별 의존성 선언
                                }
                                """.trimIndent()
                        }
                        file.writeText(content)
                        println("Created file: $file")
                    }
                    filePath.endsWith("application.yml") -> {
                        val modulePath = filePath.substringAfter("$serviceName/")
                        val content = when {
                            modulePath.startsWith("core") -> """
                                spring:
                                  application:
                                    name: ${serviceName}-core

                                # 데이터베이스 설정 (예시)
                                spring:
                                  datasource:
                                    url: jdbc:postgresql://localhost:5432/${serviceName}
                                    username: user
                                    password: password
                                  jpa:
                                    hibernate:
                                      ddl-auto: update
                                    show-sql: true
                                """.trimIndent()
                            modulePath.startsWith("adapter-inbound") -> """
                                spring:
                                  application:
                                    name: ${serviceName}-adapter-inbound
                                """.trimIndent()
                            modulePath.startsWith("adapter-outbound") -> """
                                spring:
                                  application:
                                    name: ${serviceName}-adapter-outbound
                                """.trimIndent()
                            modulePath.startsWith("shared") -> """
                                spring:
                                  application:
                                    name: ${serviceName}-shared
                                """.trimIndent()
                            else -> """
                                spring:
                                  application:
                                    name: $serviceName
                                """.trimIndent()
                        }
                        file.writeText(content)
                        println("Created file: $file")
                    }
                    else -> {
                        // 기본 템플릿 (필요 시 확장 가능)
                        file.writeText("// TODO: Implement $filePath")
                        println("Created file: $file with placeholder content")
                    }
                }
            } else {
                println("File already exists: $file")
            }
        }

        // 공통 모듈의 application.yml 생성
        val commonAppYml = file("${baseDir}/common-shared/src/main/resources/application.yml")
        if (!commonAppYml.exists()) {
            commonAppYml.parentFile.mkdirs()
            commonAppYml.createNewFile()
            commonAppYml.writeText(
                """
                spring:
                  application:
                    name: common-shared

                # 공통 설정 (예시)
                """.trimIndent()
            )
            println("Created common-shared application.yml: $commonAppYml")
        } else {
            println("common-shared application.yml already exists: $commonAppYml")
        }
    }
}