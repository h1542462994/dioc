@file:Suppress("PropertyName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `maven-publish`
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization") version "1.7.0"
}

val kotlin_version: String by project
val kotlin_serialization_version: String by project
val kotlin_coroutines_version: String by project

val junit_version: String by project
val mockito_version: String by project

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/google")
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

subprojects {
    group = "org.tty.dioc"
    version = "1.0.0-rc02"

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.gradle.maven-publish")

    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/public")
        mavenCentral()
    }

    dependencies {

        implementation("org.jetbrains.kotlin:kotlin-stdlib:${kotlin_version}")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlin_version}")
        implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlin_version}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines_version")

        testImplementation("org.junit.jupiter:junit-jupiter-api:${junit_version}")
        testImplementation("org.junit.jupiter:junit-jupiter-params:${junit_version}")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junit_version}")
        testImplementation("org.mockito:mockito-core:${mockito_version}")
    }

    java {
        withSourcesJar()
        withJavadocJar()
    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.javadoc {
        if (JavaVersion.current().isJava9Compatible) {
            (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
        }
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                groupId = project.group.toString()
                artifactId = "dioc-" + project.name
                version = project.version.toString()
                from(components["java"])
            }

            repositories {
                mavenLocal()
            }
        }
    }
}


