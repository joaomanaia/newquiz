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
    }
}

plugins {
    id("com.android.application") version "8.0.0-alpha10" apply false
    id("com.android.library") version "8.0.0-alpha10" apply false
    id("org.jetbrains.kotlin.android") apply false version "1.7.21"
    id("org.jetbrains.kotlin.plugin.serialization") apply false
    id("com.google.dagger.hilt.android") apply false
    id("com.google.gms.google-services") apply false
    id("com.android.dynamic-feature") version "8.0.0-alpha10" apply false
}

tasks.register("clean", Delete::class) {
    delete (rootProject.buildDir)
}