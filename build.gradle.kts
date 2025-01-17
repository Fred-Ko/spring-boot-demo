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

// group = "com.restaurant" // 슬래시 대신 점 사용

// tasks.register("createMicroserviceModule") {
//     group = "setup"
//     description = "Creates the folder structure and essential files for a specified service with CQRS and Hexagonal Architecture"

//     // 서비스 이름과 베이스 디렉토리를 필수 인자로 받습니다.
//     val baseDirPath: String? = project.findProperty("baseDir") as String?
//     val serviceName: String? = project.findProperty("serviceName") as String?

//     doLast {
//         if (baseDirPath == null) {
//             println("Error: baseDir is not set")
//             throw GradleException("baseDir is not set")
//         }

//         if (serviceName == null) {
//             println("Error: serviceName is not set")
//             throw GradleException("serviceName is not set")
//         }

//         val baseDir = file("$baseDirPath/$serviceName")

//         if (!baseDir.exists()) {
//             baseDir.mkdirs()
//             println("Created base directory: $baseDir")
//         } else {
//             println("Base directory already exists: $baseDir")
//         }

//         // Define the list of directories to create
//         val directories = listOf(
//             "adapter/src/main/kotlin/com/restaurant/$serviceName/adapter/inbound/api",
//             "adapter/src/main/kotlin/com/restaurant/$serviceName/adapter/outbound/api",
//             "adapter/src/main/kotlin/com/restaurant/$serviceName/adapter/outbound/persistence/entity",
//             "adapter/src/main/kotlin/com/restaurant/$serviceName/adapter/outbound/persistence/repository",
//             "adapter/src/main/resources",
//             "core/src/main/kotlin/com/restaurant/$serviceName/core/application/command/handler",
//             "core/src/main/kotlin/com/restaurant/$serviceName/core/application/command/command",
//             "core/src/main/kotlin/com/restaurant/$serviceName/core/application/query/handler",
//             "core/src/main/kotlin/com/restaurant/$serviceName/core/application/query/query",
//             "core/src/main/kotlin/com/restaurant/$serviceName/core/domain/model",
//             "core/src/main/kotlin/com/restaurant/$serviceName/core/domain/repository",
//             "core/src/main/kotlin/com/restaurant/$serviceName/core/domain/service",
//             "core/src/main/resources",
//             "shared/src/main/kotlin/com/restaurant/$serviceName/shared/config",
//             "shared/src/main/kotlin/com/restaurant/$serviceName/shared/dto",
//             "shared/src/main/kotlin/com/restaurant/$serviceName/shared/util",
//             "shared/src/main/resources",
//             "test/src"
//         )

//         // Create each directory if it doesn't exist
//         directories.forEach { dirPath ->
//             val dir = file("$baseDir/$dirPath")
//             if (!dir.exists()) {
//                 dir.mkdirs()
//                 println("Created directory: $dir")
//             } else {
//                 println("Directory already exists: $dir")
//             }
//         }

//         // Define the list of essential files to create
//         val files = listOf(
//             // Root of service
//             "$baseDir/build.gradle.kts",

//             // Core module
//             "$baseDir/core/build.gradle.kts",

//             // Adapter module
//             "$baseDir/adapter/build.gradle.kts",

//             // Shared module
//             "$baseDir/shared/build.gradle.kts",

//             // Test module
//             "$baseDir/test/build.gradle.kts",

//             // Core application.yml
//             "$baseDir/core/src/main/resources/application.yml",

//             // Adapter application.yml
//             "$baseDir/adapter/src/main/resources/application.yml",

//             // Shared application.yml
//             "$baseDir/shared/src/main/resources/application.yml"
//         )

//         // Create each file with template content if it doesn't exist
//         files.forEach { filePath ->
//             val file = file(filePath)
//             if (!file.exists()) {
//                 file.parentFile.mkdirs() // Ensure parent directories exist
//                 file.createNewFile()
//                 // Write template content based on the file type
//                 when {
//                     filePath.endsWith("build.gradle.kts") -> {
//                         val moduleName = filePath.substringAfter("$serviceName/").substringBefore("/")
//                         val content = when (moduleName) {
//                             "core" -> """
//                                 plugins {
//                                     kotlin("jvm")
//                                 }

//                                 dependencies {
//                                     implementation(project(":common-shared"))
//                                 }
//                                 """.trimIndent()
//                             "adapter" -> """
//                                 plugins {
//                                     kotlin("jvm")
//                                 }

//                                 dependencies {
//                                     implementation(project(":core"))
//                                     implementation("org.springframework.boot:spring-boot-starter-web")
//                                     // 추가적인 의존성 선언
//                                 }
//                                 """.trimIndent()
//                             "shared" -> """
//                                 plugins {
//                                     kotlin("jvm")
//                                 }

//                                 dependencies {
//                                     implementation(project(":common-shared"))
//                                     // 추가적인 의존성 선언
//                                 }
//                                 """.trimIndent()
//                             "test" -> """
//                                 plugins {
//                                     kotlin("jvm")
//                                 }

//                                 dependencies {
//                                     implementation(project(":core"))
//                                     implementation("org.springframework.boot:spring-boot-starter-test")
//                                     // 추가적인 의존성 선언
//                                 }
//                                 """.trimIndent()
//                             else -> """
//                                 plugins {
//                                     kotlin("jvm")
//                                 }

//                                 dependencies {
//                                     // 모듈별 의존성 선언
//                                 }
//                                 """.trimIndent()
//                         }
//                         file.writeText(content)
//                         println("Created file: $file")
//                     }
//                     filePath.endsWith("application.yml") -> {
//                         val modulePath = filePath.substringAfter("$serviceName/")
//                         val content = when {
//                             modulePath.startsWith("core") -> """
//                                 spring:
//                                   application:
//                                     name: ${serviceName}-core

//                                 # 데이터베이스 설정 (예시)
//                                 spring:
//                                   datasource:
//                                     url: jdbc:postgresql://localhost:5432/${serviceName}
//                                     username: user
//                                     password: password
//                                   jpa:
//                                     hibernate:
//                                       ddl-auto: update
//                                     show-sql: true
//                                 """.trimIndent()
//                             modulePath.startsWith("adapter") -> """
//                                 spring:
//                                   application:
//                                     name: ${serviceName}-adapter
//                                 """.trimIndent()
//                             modulePath.startsWith("shared") -> """
//                                 spring:
//                                   application:
//                                     name: ${serviceName}-shared
//                                 """.trimIndent()
//                             else -> """
//                                 spring:
//                                   application:
//                                     name: $serviceName
//                                 """.trimIndent()
//                         }
//                         file.writeText(content)
//                         println("Created file: $file")
//                     }
//                     else -> {
//                         // 기본 템플릿 (필요 시 확장 가능)
//                         file.writeText("// TODO: Implement $filePath")
//                         println("Created file: $file with placeholder content")
//                     }
//                 }
//             } else {
//                 println("File already exists: $file")
//             }
//         }

//         // settings.gradle.kts 파일 수정
//         val settingsFile = file("${project.rootDir}/settings.gradle.kts")
//         if (settingsFile.exists()) {
//             val currentContent = settingsFile.readText()
//             val moduleIncludes = listOf(
//                 "\"$serviceName\"",
//                 "\"$serviceName:adapter\"",
//                 "\"$serviceName:core\"",
//                 "\"$serviceName:shared\"",
//                 "\"$serviceName:test\""
//             )

//             val newIncludes = moduleIncludes.joinToString("\n") { "include($it)" }

//             if (!currentContent.contains("include(\"$serviceName\")")) {
//                 settingsFile.appendText("""
// // $serviceName 모듈
// $newIncludes

//                 """)
//                 println("Added $serviceName modules to settings.gradle.kts")
//             }
//         } else {
//             println("Warning: settings.gradle.kts file not found!")
//         }
//     }
// }

// ===============================================================================================================================

// ==================================================
// 1. 마이크로서비스 모듈 생성 Task
// ==================================================
tasks.register("createMicroserviceModule") {
    group = "setup"
    description = "Creates the folder structure and essential files for a specified service with CQRS and Hexagonal Architecture"

    // 서비스 이름과 베이스 디렉토리를 필수 인자로 받습니다.
    val baseDirPath: String? = project.findProperty("baseDir") as String?
    val serviceName: String? = project.findProperty("serviceName") as String?

    // 템플릿 기반 build.gradle.kts 내용을 반환하는 함수
    fun buildGradleTemplate(moduleName: String): String = when (moduleName) {
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

    // 템플릿 기반 application.yml 내용을 반환하는 함수
    fun applicationYmlTemplate(modulePath: String): String = when {
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

    // 디렉토리 생성 함수
    fun createDirectories(baseDir: File, directories: List<String>) {
        directories.forEach { dirPath ->
            val dir = file("$baseDir/$dirPath")
            if (!dir.exists()) {
                dir.mkdirs()
                println("Created directory: $dir")
            } else {
                println("Directory already exists: $dir")
            }
        }
    }

    // 파일 생성 및 템플릿 적용 함수
    fun createFiles(baseDir: File, files: List<String>) {
        files.forEach { filePathStr ->
            val targetFile = file(filePathStr)
            if (!targetFile.exists()) {
                targetFile.parentFile.mkdirs()  // 부모 디렉토리 생성
                targetFile.createNewFile()

                when {
                    filePathStr.endsWith("build.gradle.kts") -> {
                        // serviceName/모듈명 이후의 첫 번째 경로를 모듈 이름으로 사용
                        val moduleName = filePathStr.substringAfter("${serviceName}/").substringBefore("/")
                        targetFile.writeText(buildGradleTemplate(moduleName))
                        println("Created file: $targetFile")
                    }
                    filePathStr.endsWith("application.yml") -> {
                        // serviceName/ 이후 경로를 통해 모듈 식별
                        val modulePath = filePathStr.substringAfter("${serviceName}/")
                        targetFile.writeText(applicationYmlTemplate(modulePath))
                        println("Created file: $targetFile")
                    }
                    else -> {
                        targetFile.writeText("// TODO: Implement $filePathStr")
                        println("Created file: $targetFile with placeholder content")
                    }
                }
            } else {
                println("File already exists: $targetFile")
            }
        }
    }

    // settings.gradle.kts에 모듈 include를 추가하는 함수
    fun updateSettingsGradle(serviceName: String) {
        val settingsFile = file("${project.rootDir}/settings.gradle.kts")
        if (settingsFile.exists()) {
            val currentContent = settingsFile.readText()
            // include 대상 모듈 목록
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

                """.trimIndent())
                println("Added $serviceName modules to settings.gradle.kts")
            }
        } else {
            println("Warning: settings.gradle.kts file not found!")
        }
    }

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

        // 생성할 디렉토리 목록
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
        createDirectories(baseDir, directories)

        // 생성할 파일 목록
        val files = listOf(
            // 루트 module build 파일
            "$baseDir/build.gradle.kts",
            // 각 모듈 build 파일
            "$baseDir/core/build.gradle.kts",
            "$baseDir/adapter/build.gradle.kts",
            "$baseDir/shared/build.gradle.kts",
            "$baseDir/test/build.gradle.kts",
            // 각 모듈 application.yml 파일
            "$baseDir/core/src/main/resources/application.yml",
            "$baseDir/adapter/src/main/resources/application.yml",
            "$baseDir/shared/src/main/resources/application.yml"
        )
        createFiles(baseDir, files)
        updateSettingsGradle(serviceName)
    }
}

// ==================================================
// 2. 공통 모듈 (common-shared) 생성 Task
// ==================================================
tasks.register("createCommonModule") {
    group = "setup"
    description = "Creates the folder structure and essential files for the common module"

    // 공통 모듈의 베이스 디렉토리와 모듈 이름 설정
    val baseDirPath: String? = project.findProperty("baseDir") as String?
    val commonModuleName = "common-shared"

    // 공통 모듈 전용 build.gradle.kts 템플릿 내용
    fun buildGradleTemplateForCommon(): String = """
        plugins {
            kotlin("jvm")
        }
        
        dependencies {
            // 공통으로 사용할 의존성 선언
        }
        """.trimIndent()

    // 디렉토리 생성 함수 (마이크로서비스 모듈의 함수와 동일)
    fun createDirectories(baseDir: File, directories: List<String>) {
        directories.forEach { dirPath ->
            val dir = file("$baseDir/$dirPath")
            if (!dir.exists()) {
                dir.mkdirs()
                println("Created directory: $dir")
            } else {
                println("Directory already exists: $dir")
            }
        }
    }

    // 파일 생성 함수 (공통 모듈 전용)
    fun createFiles(baseDir: File, files: List<String>) {
        files.forEach { filePathStr ->
            val targetFile = file(filePathStr)
            if (!targetFile.exists()) {
                targetFile.parentFile.mkdirs()  // 부모 디렉토리 생성
                targetFile.createNewFile()
                when {
                    filePathStr.endsWith("build.gradle.kts") -> {
                        targetFile.writeText(buildGradleTemplateForCommon())
                        println("Created file: $targetFile")
                    }
                    else -> {
                        targetFile.writeText("// TODO: Implement $filePathStr")
                        println("Created file: $targetFile with placeholder content")
                    }
                }
            } else {
                println("File already exists: $targetFile")
            }
        }
    }

    // settings.gradle.kts에 모듈 include 추가하는 함수
    fun updateSettingsGradle(moduleName: String) {
        val settingsFile = file("${project.rootDir}/settings.gradle.kts")
        if (settingsFile.exists()) {
            val currentContent = settingsFile.readText()
            if (!currentContent.contains("include(\"$moduleName\")")) {
                settingsFile.appendText("""

                    // $moduleName 모듈
                    include("$moduleName")
                    
                """.trimIndent())
                println("Added $moduleName module to settings.gradle.kts")
            }
        } else {
            println("Warning: settings.gradle.kts file not found!")
        }
    }

    doLast {
        if (baseDirPath == null) {
            println("Error: baseDir is not set")
            throw GradleException("baseDir is not set")
        }
        val baseDir = file("$baseDirPath/$commonModuleName")
        if (!baseDir.exists()) {
            baseDir.mkdirs()
            println("Created base directory: $baseDir")
        } else {
            println("Base directory already exists: $baseDir")
        }

        // 공통 모듈에서 필요한 디렉토리 생성 (예: src/main 및 src/test)
        val directories = listOf(
            "src/main/kotlin/com/restaurant/$commonModuleName",
            "src/main/resources",
            "src/test/kotlin/com/restaurant/$commonModuleName"
        )
        createDirectories(baseDir, directories)

        // 공통 모듈에서 생성할 파일 목록
        val files = listOf(
            "$baseDir/build.gradle.kts",
            "$baseDir/src/main/resources/application.yml"
        )
        createFiles(baseDir, files)
        updateSettingsGradle(commonModuleName)
    }
}

// ==================================================
// 3. 독립 라이브러리 모듈 생성 Task (라이브러리화 가능)
// ==================================================
tasks.register("createLibraryModule") {
    group = "setup"
    description = "Creates the folder structure and essential files for an independent library module"

    val baseDirPath: String? = project.findProperty("baseDir") as String?
    val libName: String? = project.findProperty("libName") as String? ?: "library-module"

    // 라이브러리 전용 build.gradle.kts 템플릿 내용
    fun buildGradleTemplateForLibrary(): String = """
        plugins {
            kotlin("jvm")
        }
        
        dependencies {
            // 이 라이브러리 모듈에서 사용할 의존성 선언
        }
        """.trimIndent()

    // 디렉토리 생성 함수 (다른 Task와 동일)
    fun createDirectories(baseDir: File, directories: List<String>) {
        directories.forEach { dirPath ->
            val dir = file("$baseDir/$dirPath")
            if (!dir.exists()) {
                dir.mkdirs()
                println("Created directory: $dir")
            } else {
                println("Directory already exists: $dir")
            }
        }
    }

    // 파일 생성 함수 (라이브러리 모듈 전용)
    fun createFiles(baseDir: File, files: List<String>) {
        files.forEach { filePathStr ->
            val targetFile = file(filePathStr)
            if (!targetFile.exists()) {
                targetFile.parentFile.mkdirs() // 부모 디렉토리 생성
                targetFile.createNewFile()
                when {
                    filePathStr.endsWith("build.gradle.kts") -> {
                        targetFile.writeText(buildGradleTemplateForLibrary())
                        println("Created file: $targetFile")
                    }
                    else -> {
                        targetFile.writeText("// TODO: Implement $filePathStr")
                        println("Created file: $targetFile with placeholder content")
                    }
                }
            } else {
                println("File already exists: $targetFile")
            }
        }
    }

    // settings.gradle.kts에 모듈 include 추가 함수
    fun updateSettingsGradle(moduleName: String) {
        val settingsFile = file("${project.rootDir}/settings.gradle.kts")
        if (settingsFile.exists()) {
            val currentContent = settingsFile.readText()
            if (!currentContent.contains("include(\"$moduleName\")")) {
                settingsFile.appendText("""

                    // $moduleName 모듈
                    include("$moduleName")
                    
                """.trimIndent())
                println("Added $moduleName module to settings.gradle.kts")
            }
        } else {
            println("Warning: settings.gradle.kts file not found!")
        }
    }

    doLast {
        if (baseDirPath == null) {
            println("Error: baseDir is not set")
            throw GradleException("baseDir is not set")
        }

        val baseDir = file("$baseDirPath/$libName")
        if (!baseDir.exists()) {
            baseDir.mkdirs()
            println("Created base directory for library module: $baseDir")
        } else {
            println("Library module base directory already exists: $baseDir")
        }

        // 라이브러리 모듈에서 필요한 디렉토리 생성 (예: src/main 및 src/test)
        val directories = listOf(
            "src/main/kotlin/com/$libName",
            "src/main/resources",
            "src/test/kotlin/com/$libName"
        )
        createDirectories(baseDir, directories)

        // 라이브러리 모듈에서 생성할 파일 목록
        val files = listOf(
            "$baseDir/build.gradle.kts",
            "$baseDir/src/main/resources/application.yml"
        )
        createFiles(baseDir, files)
        updateSettingsGradle(libName!!)
    }
}
