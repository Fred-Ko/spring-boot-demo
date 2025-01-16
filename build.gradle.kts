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
            "adapter/src/main/kotlin/com/restaurant/$serviceName/adapter/inbound/api",
            "adapter/src/main/kotlin/com/restaurant/$serviceName/adapter/outbound/api",
            "adapter/src/main/kotlin/com/restaurant/$serviceName/adapter/outbound/persistence/entity",
            "adapter/src/main/kotlin/com/restaurant/$serviceName/adapter/outbound/persistence/repository",
            "adapter/src/main/resources",
            "core/src/main/kotlin/com/restaurant/$serviceName/core/application/command/handler",
            "core/src/main/kotlin/com/restaurant/$serviceName/core/application/command/command",
            "core/src/main/kotlin/com/restaurant/$serviceName/core/application/query/handler",
            "core/src/main/kotlin/com/restaurant/$serviceName/core/application/query/query",
            "core/src/main/kotlin/com/restaurant/$serviceName/core/domain/model",
            "core/src/main/kotlin/com/restaurant/$serviceName/core/domain/repository",
            "core/src/main/kotlin/com/restaurant/$serviceName/core/domain/service",
            "core/src/main/resources",
            "shared/src/main/kotlin/com/restaurant/$serviceName/shared/config",
            "shared/src/main/kotlin/com/restaurant/$serviceName/shared/dto",
            "shared/src/main/kotlin/com/restaurant/$serviceName/shared/util",
            "shared/src/main/resources",
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

            // Adapter module
            "$baseDir/adapter/build.gradle.kts",

            // Shared module
            "$baseDir/shared/build.gradle.kts",

            // Test module
            "$baseDir/test/build.gradle.kts",

            // Core application.yml
            "$baseDir/core/src/main/resources/application.yml",

            // Adapter application.yml
            "$baseDir/adapter/src/main/resources/application.yml",

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
                            "adapter" -> """
                                plugins {
                                    kotlin("jvm")
                                }

                                dependencies {
                                    implementation(project(":core"))
                                    implementation("org.springframework.boot:spring-boot-starter-web")
                                    // 추가적인 의존성 선언
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
                            modulePath.startsWith("adapter") -> """
                                spring:
                                  application:
                                    name: ${serviceName}-adapter
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

        // settings.gradle.kts 파일 수정
        val settingsFile = file("${project.rootDir}/settings.gradle.kts")
        if (settingsFile.exists()) {
            val currentContent = settingsFile.readText()
            val moduleIncludes = listOf(
                "\"$serviceName\"",
                "\"$serviceName:adapter\"",
                "\"$serviceName:core\"",
                "\"$serviceName:shared\"",
                "\"$serviceName:test\""
            )

            val newIncludes = moduleIncludes.joinToString("\n") { "include($it)" }

            if (!currentContent.contains("include(\"$serviceName\")")) {
                settingsFile.appendText("""
// $serviceName 모듈
$newIncludes

                """)
                println("Added $serviceName modules to settings.gradle.kts")
            }
        } else {
            println("Warning: settings.gradle.kts file not found!")
        }
    }
}