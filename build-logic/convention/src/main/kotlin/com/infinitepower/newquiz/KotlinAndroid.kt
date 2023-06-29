package com.infinitepower.newquiz

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, * , * , *, *>
) {
    commonExtension.apply {
        compileSdk = ProjectConfig.compileSdk

        defaultConfig {
            minSdk = ProjectConfig.minSdk
        }

        compileOptions {
            sourceCompatibility = ProjectConfig.javaVersionCompatibility
            targetCompatibility = ProjectConfig.javaVersionCompatibility
        }
    }

    configureKotlin()
}

internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = ProjectConfig.javaVersionCompatibility
        targetCompatibility = ProjectConfig.javaVersionCompatibility
    }

    extensions.configure<KotlinJvmProjectExtension> {
        jvmToolchain(ProjectConfig.jvmToolchainVersion)
    }
}

private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = ProjectConfig.jvmTargetVersion

            freeCompilerArgs += listOf(
                "-opt-in=kotlin.RequiresOptIn"
            )
        }
    }
}