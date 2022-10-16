buildscript {
    val kotlin_version by extra("1.7.20")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Firebase.crashlyticsGradlePlugin)
        classpath(Firebase.performanceMonitoringGradlePlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

plugins {
    id("com.android.application") version "7.4.0-beta02" apply false
    id("com.android.library") version "7.4.0-beta02" apply false
    id("org.jetbrains.kotlin.android") apply false
    id("org.jetbrains.kotlin.plugin.serialization") apply false
    id("com.google.dagger.hilt.android") apply false
    id("com.google.gms.google-services") apply false
    id("com.android.dynamic-feature") version "7.4.0-beta02" apply false
}

tasks.register("clean", Delete::class) {
    delete (rootProject.buildDir)
}