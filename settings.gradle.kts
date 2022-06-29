pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.40.2"
}

rootProject.name = "NewQuiz"
include(":app")
include(":core")
include(":model")
include(":domain")
include(":data")
include(":home-presentation")
include(":quiz-presentation")
include(":settings-presentation")
