buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Firebase.crashlyticsGradlePlugin)
        classpath(Firebase.performanceMonitoringGradlePlugin)
    }
}

plugins {
    id("com.android.application") version "7.4.0-alpha05" apply false
    id("com.android.library") version "7.4.0-alpha05" apply false
    id("org.jetbrains.kotlin.android") apply false
    id("org.jetbrains.kotlin.plugin.serialization") apply false
    id("com.google.dagger.hilt.android") apply false
    id("com.google.gms.google-services") apply false
    id("com.android.dynamic-feature") version "7.4.0-alpha09" apply false
}

tasks.register("clean", Delete::class) {
    delete (rootProject.buildDir)
}