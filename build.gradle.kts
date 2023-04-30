buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Firebase.crashlyticsGradlePlugin)
        classpath(Firebase.performanceMonitoringGradlePlugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.oss.licenses.plugin)
    }
}

plugins {
    id("com.android.application") version "8.1.0-beta01" apply false
    id("com.android.library") version "8.1.0-beta01" apply false
    id("org.jetbrains.kotlin.android") apply false version "1.8.10"
    id("org.jetbrains.kotlin.plugin.serialization") apply false
    id("com.google.dagger.hilt.android") apply false
    id("com.google.gms.google-services") apply false
    id("com.android.dynamic-feature") version "8.1.0-beta01" apply false
}

tasks.register("clean", Delete::class) {
    delete (rootProject.buildDir)
}