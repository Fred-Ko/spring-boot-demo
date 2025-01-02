import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.api.tasks.Exec
import java.io.IOException

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

group = "com.restaurant"
version = "3.4.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.kafka:spring-kafka")
    // implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.add("-Xjvm-default=all")
        }
    }

    tasks.withType<JavaCompile> {
        options.release.set(21)
    }

    tasks.withType<org.gradle.api.tasks.testing.Test> {
        useJUnitPlatform()
    }
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/main/kotlin"))
        }
    }
}

// ===================== 유틸 함수들 ===================== //

// 루트 settings.gradle.kts 파일 객체
val settingsFile = File(rootProject.projectDir, "settings.gradle.kts")
val groupName = "com.restaurant"
/** 
 * settings.gradle.kts가 없으면 자동 생성.
 */
fun ensureSettingsFile() {
    if (!settingsFile.exists()) {
        println("settings.gradle.kts가 없어 새로 생성합니다.")
        settingsFile.writeText(
            """
            // 자동 생성된 settings.gradle.kts
            rootProject.name = "${rootProject.name}"
            """.trimIndent()
        )
    }
}

/**
 * settings.gradle.kts에 include(":xxx") 추가 (중복 검사).
 * 디렉토리가 없거나 파일인 경우 등은 확인하지 않고 단순 라인 추가만 담당.
 */
fun addModuleInclude(modulePath: String) {
    ensureSettingsFile()
    val content = settingsFile.readText()
    val includeLine = """include("$modulePath")"""
    if (content.contains(includeLine)) {
        println("이미 포함된 모듈입니다: $includeLine")
    } else {
        settingsFile.appendText("\n$includeLine\n")
        println("settings.gradle.kts에 모듈을 추가했습니다: $includeLine")
    }
}

/**
 * 디렉토리를 만들고, 이미 파일이 있으면 에러 대신 로그만 남기고 SKIP.
 */
fun safeMkdirs(dir: File): Boolean {
    if (dir.exists()) {
        return if (dir.isDirectory) {
            // 이미 디렉토리가 있는 경우 OK
            true
        } else {
            // 파일이 존재하는 경우
            println("⚠️ 경로가 디렉토리가 아닌 파일입니다: ${dir.absolutePath}")
            println("   디렉토리 생성 불가하므로 스킵합니다. (원치 않으면 수동 조치 필요)")
            false
        }
    } else {
        val result = dir.mkdirs()
        if (result) {
            println("디렉토리를 생성했습니다: ${dir.absolutePath}")
        } else {
            println("⚠️ 디렉토리 생성 실패: ${dir.absolutePath}")
        }
        return result
    }
}

/**
 * build.gradle.kts를 생성하되, 이미 존재하면 덮어쓰지 않고 로그만 출력.
 */
fun createBuildGradleKts(moduleDir: File, extraContent: String = "") {
    // 디렉토리가 없으면 생성 시도
    if (!safeMkdirs(moduleDir)) {
        println("⚠️ createBuildGradleKts: '${moduleDir.name}' 디렉토리를 준비하지 못했으므로 스킵합니다.")
        return
    }

    val buildGradleKts = File(moduleDir, "build.gradle.kts")
    if (buildGradleKts.exists()) {
        // 이미 존재하면 건드리지 않음
        println("build.gradle.kts가 이미 존재합니다: ${buildGradleKts.path}")
    } else {
        println("build.gradle.kts를 생성합니다: ${buildGradleKts.path}")
        buildGradleKts.writeText(
            """
            // 자동 생성된 build.gradle.kts
            plugins {
                kotlin("jvm") version "$kotlinVersion"
                kotlin("plugin.spring") version "$kotlinVersion"
                id("org.springframework.boot") version "$springBootVersion"
                id("io.spring.dependency-management") version "$dependencyManagementVersion"
            }
            
            repositories {
                mavenCentral()
            }
            
            dependencies {
                // 필요한 의존성을 추가하세요
            }
            
            $extraContent
            """.trimIndent()
        )
    }
}

/**
 * 기본 소스 디렉토리 구조 생성 (src/main/kotlin, src/test/kotlin 등).
 */
fun createDefaultSourceDirs(baseDir: File, moduleName: String, sub: String? = null) {
    // moduleName을 사용하여 패키지 경로 설정
    val packagePath = "$groupName.$moduleName".replace('.', '/')
    
    val dirs = listOf(
        "src/main/kotlin/$packagePath/$sub",
        "src/main/resources",
        "src/test/kotlin/$packagePath/$sub",
        "src/test/resources"
    )
    
    dirs.forEach { relativePath ->
        val dirFile = File(baseDir, relativePath)
        if (safeMkdirs(dirFile)) {
            println("디렉토리 존재(또는 생성) 확인됨: ${dirFile.path}")
        } else {
            println("⚠️ 소스 디렉토리를 준비하지 못했습니다: ${dirFile.path}")
        }
    }
}

/**
 * Spring Boot 메인 클래스를 생성합니다.
 */
fun createSpringBootMainClass(subModuleDir: File, microserviceName: String, sub: String) {
    // 원하는 경로로 수정
    val mainKt = File(subModuleDir, "src/main/kotlin/${groupName.split('.').joinToString("/")}/$microserviceName/$sub/Application.kt")
    if (!mainKt.exists()) {
        if (!safeMkdirs(mainKt.parentFile)) {
            println("⚠️ Application.kt 생성 실패 (디렉토리 미존재).")
            return
        }
        println("Spring Boot 메인 클래스를 생성합니다: ${mainKt.path}")
        mainKt.writeText(
            """
            package $microserviceName.app

            import org.springframework.boot.autoconfigure.SpringBootApplication
            import org.springframework.boot.runApplication

            @SpringBootApplication
            open class Application

            fun main(args: Array<String>) {
                runApplication<Application>(*args)
            }
            """.trimIndent()
        )
    } else {
        println("이미 Spring Boot 메인 클래스 파일이 존재합니다: ${mainKt.path}")
    }
}

// ===================== Task들 ===================== //

// 1. initializeProject Task
tasks.register("initializeProject") {
    group = "project-setup"
    description = "프로젝트의 초기 설정을 수행하여 멀티 모듈 환경을 준비합니다."

    doLast {
        val rootDir = rootProject.projectDir
        val buildKtsFile = File(rootDir, "build.gradle.kts")
        val settingsKtsFile = File(rootDir, "settings.gradle.kts")

        if (!buildKtsFile.exists()) {
            buildKtsFile.writeText(
                """
                // 자동 생성된 루트 build.gradle.kts
                plugins {
                    // 필요 시 루트 프로젝트용 플러그인 추가
                }
                
                allprojects {
                    repositories {
                        mavenCentral()
                    }
                }
                
                """.trimIndent()
            )
            println("루트 build.gradle.kts 파일을 생성했습니다: ${buildKtsFile.path}")
        } else {
            println("루트 build.gradle.kts 파일이 이미 존재합니다: ${buildKtsFile.path}")
        }

        if (!settingsKtsFile.exists()) {
            settingsKtsFile.writeText(
                """
                // 자동 생성된 settings.gradle.kts
                rootProject.name = "${rootProject.name}"
                """.trimIndent()
            )
            println("settings.gradle.kts 파일을 생성했습니다: ${settingsKtsFile.path}")
        } else {
            println("settings.gradle.kts 파일이 이미 존재합니다: ${settingsKtsFile.path}")
        }

        println("프로젝트 초기 설정이 완료되었습니다.")
    }
}

// 2. addModuleToSettings Task
tasks.register("addModuleToSettings") {
    group = "project-setup"
    description = "새로운 모듈을 settings.gradle.kts에 포함시켜 빌드에 인식되도록 합니다."

    doLast {
        val moduleName = project.findProperty("moduleName")?.toString() ?: run {
            throw GradleException("모듈 이름이 지정되지 않았습니다. -PmoduleName=xxx 형태로 전달하세요.")
        }
        addModuleInclude(":$moduleName")
        println("addModuleToSettings 수행 완료.")
    }
}

// 3. createIndependentModule Task
tasks.register("createIndependentModule") {
    group = "project-setup"
    description = "독립적인 기능을 수행하는 모듈을 생성하고, settings.gradle.kts에 등록합니다."

    doLast {
        val moduleName = project.findProperty("moduleName")?.toString()
            ?: throw GradleException("모듈 이름이 지정되지 않았습니다. -PmoduleName=xxx 형태로 전달하세요.")
        val baseDirProp = project.findProperty("baseDir")?.toString() ?: ""

        // baseDir이 절대 경로인지 확인
        val baseDirFile = File(baseDirProp)
        val finalBaseDir = if (baseDirFile.isAbsolute) {
            baseDirFile
        } else {
            File(rootProject.projectDir, baseDirProp)
        }

        val moduleDir = File(finalBaseDir, moduleName)

        // 모듈 디렉토리 생성
        if (!safeMkdirs(moduleDir)) {
            println("⚠️ 모듈 디렉토리를 준비하지 못했으므로 작업을 중단합니다.")
            return@doLast
        }

        // build.gradle.kts 생성
        createBuildGradleKts(moduleDir)

        // 기본 소스 디렉토리 구조 생성
        createDefaultSourceDirs(moduleDir, moduleName)

        // settings.gradle.kts에 모듈 등록
        addModuleInclude(":${finalBaseDir.relativeTo(rootProject.projectDir).path.replace(File.separator, ":")}:$moduleName")

        println("createIndependentModule 작업이 완료되었습니다.")
    }
}

// 4. createCommonModule Task
tasks.register("createCommonModule") {
    group = "project-setup"
    description = "여러 모듈에서 공통으로 사용하는 기능이나 설정을 담은 모듈을 생성하고, settings.gradle.kts에 등록합니다."

    doLast {
        val moduleName = project.findProperty("moduleName")?.toString()
            ?: throw GradleException("공통 모듈 이름이 지정되지 않았습니다. -PmoduleName=xxx 형태로 전달하세요.")
        val baseDirProp = project.findProperty("baseDir")?.toString() ?: ""

        // baseDir이 절대 경로인지 확인
        val baseDirFile = File(baseDirProp)
        val finalBaseDir = if (baseDirFile.isAbsolute) {
            baseDirFile
        } else {
            File(rootProject.projectDir, baseDirProp)
        }

        val moduleDir = File(finalBaseDir, moduleName)

        // 공통 모듈 디렉토리 생성
        if (!safeMkdirs(moduleDir)) {
            println("⚠️ 공통 모듈 디렉토리를 준비하지 못했으므로 작업을 중단합니다.")
            return@doLast
        }

        // build.gradle.kts 생성
        createBuildGradleKts(moduleDir)

        // 기본 소스 디렉토리 구조 생성
        createDefaultSourceDirs(moduleDir, moduleName)

        // settings.gradle.kts에 모듈 등록
        addModuleInclude(":${finalBaseDir.relativeTo(rootProject.projectDir).path.replace(File.separator, ":")}:$moduleName")

        println("createCommonModule 작업이 완료되었습니다.")
    }
}

// 5. createMicroserviceModule Task
tasks.register("createMicroserviceModule") {
    group = "project-setup"
    description = "마이크로서비스 구조(domain/client/app)를 자동 생성하고, settings.gradle.kts에 등록합니다."

    doLast {
        val microserviceName = project.findProperty("microserviceName")?.toString()
            ?: throw GradleException("마이크로서비스 이름이 지정되지 않았습니다. -PmicroserviceName=xxx 형태로 전달하세요.")
        val baseDirProp = project.findProperty("baseDir")?.toString() ?: ""

        // baseDir이 절대 경로인지 확인
        val baseDirFile = File(baseDirProp)
        val finalBaseDir = if (baseDirFile.isAbsolute) {
            baseDirFile
        } else {
            File(rootProject.projectDir, baseDirProp)
        }

        // baseDir가 "domains/menu"처럼 이미 microserviceName으로 끝나면 중첩 방지
        val targetBaseDir = if (finalBaseDir.name == microserviceName) {
            finalBaseDir
        } else {
            File(finalBaseDir, microserviceName)
        }

        // 마이크로서비스 루트 디렉토리 생성
        if (!safeMkdirs(targetBaseDir)) {
            println("⚠️ 마이크로서비스 루트 디렉토리를 준비하지 못했으므로 작업을 중단합니다.")
            return@doLast
        }
        println("마이크로서비스 루트 디렉토리 확인됨: ${targetBaseDir.absolutePath}")

        val subModules = listOf("domain","presentation","app")
        subModules.forEach { sub ->
            val subModuleDir = File(targetBaseDir, sub)
            if (!safeMkdirs(subModuleDir)) {
                println("⚠️ '$sub' 모듈 디렉토리를 준비하지 못했으므로 스킵합니다.")
                return@forEach
            }
            createBuildGradleKts(subModuleDir)
            createDefaultSourceDirs(subModuleDir, microserviceName, sub)

            // settings.gradle.kts에 등록
            addModuleInclude(":${targetBaseDir.relativeTo(rootProject.projectDir).path.replace(File.separator, ":")}:$sub")

            // app 모듈만 따로 Spring Boot 메인 클래스 생성
            if (sub == "app") {
                createSpringBootMainClass(subModuleDir, microserviceName, sub)
            }
        }

        println("createMicroserviceModule 작업 완료.")
    }
}

// 6. updateSettingsGradle Task
tasks.register("updateSettingsGradle") {
    group = "project-setup"
    description = "생성된 모듈들을 settings.gradle.kts 파일에 포함시켜 Gradle이 인식하도록 합니다."

    doLast {
        val modulePaths = project.findProperty("modulePaths")?.toString() ?: ""
        if (modulePaths.isBlank()) {
            println("추가할 모듈 정보가 없습니다. -PmodulePaths=:moduleA,:moduleB 형태로 입력하세요.")
            return@doLast
        }

        ensureSettingsFile()

        modulePaths.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .forEach { path ->
                addModuleInclude(path)
            }

        println("updateSettingsGradle 작업이 완료되었습니다.")
    }
}
