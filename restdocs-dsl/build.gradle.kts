import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.18.0"
    id("org.jmailen.kotlinter") version "3.6.0"

    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "me.dgahn"
version = "0.1.0"

repositories {
    google()
    mavenCentral()
}

kotlinter {
    ignoreFailures = false
    indentSize = 4
    reporters = arrayOf("checkstyle", "plain")
    experimentalRules = false
    disabledRules = emptyArray()
}

val asciidoctorExt by configurations.creating

dependencies {
    /**
     * for Kotlin
     */
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    /**
     * for Spring
     */
    implementation("org.springframework.boot:spring-boot-starter-web")

    /**
     * for Logging
     */
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.2")

    /**
     * for Testing
     */
    testImplementation("org.springframework.boot:spring-boot-starter-test"){
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation("io.kotest:kotest-assertions-core:5.5.5")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")
    testImplementation("io.mockk:mockk:1.13.2")

    /**
     * for RestDocs - mockmvc
     */
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    
}

val snippetsDir by extra {
    file("build/generated-snippets")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
        dependsOn(detekt)
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    withType<Detekt> {
        dependsOn(formatKotlin)
    }

    asciidoctor {
        dependsOn(test)
        configurations("asciidoctorExt")
        baseDirFollowsSourceFile()
        inputs.dir(snippetsDir)
    }
    register<Copy>("copyDocument") {
        dependsOn(asciidoctor)
        from(file("build/docs/asciidoc/index.html"))
        into(file("src/main/resources/static/docs"))
    }
    bootJar {
        dependsOn("copyDocument")
    }
}
