package com.infinitepower.newquiz

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, * , * , *, *, *>
) {
    commonExtension.apply {
        compileSdk = ProjectConfig.compileSdk

        defaultConfig {
            minSdk = ProjectConfig.minSdk
        }

        compileOptions {
            sourceCompatibility = ProjectConfig.javaVersionCompatibility
            targetCompatibility = ProjectConfig.javaVersionCompatibility

            isCoreLibraryDesugaringEnabled = true
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
                excludes += "/META-INF/LICENSE.md"
                excludes += "/META-INF/LICENSE-notice.md"
            }
        }
    }

    configureKotlinAndroid()

    dependencies {
        coreLibraryDesugaring(libs.findLibrary("android.desugarJdkLibs").get())
    }
}

internal fun Project.configureKotlinJvm() {
    extensions.apply {
        configure<JavaPluginExtension> {
            sourceCompatibility = ProjectConfig.javaVersionCompatibility
            targetCompatibility = ProjectConfig.javaVersionCompatibility
        }

        configure<KotlinJvmProjectExtension> {
            jvmToolchain(ProjectConfig.jvmToolchainVersion)
        }
    }
}

private fun Project.configureKotlinAndroid() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17

            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }

    extensions.configure<KotlinProjectExtension> {
        jvmToolchain(ProjectConfig.jvmToolchainVersion)
    }
}
