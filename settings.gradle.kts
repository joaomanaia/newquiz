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
    id("de.fayard.refreshVersions") version "0.51.0"
}

rootProject.name = "NewQuiz"
include(":app")
include(":core")
include(":core-test")
include(":model")
include(":domain")
include(":data")
include(":multi-choice-quiz")
include(":settings-presentation")
include(":wordle")
include(":translation_dynamic_feature")
include(":online_services")
include(":maze-quiz")
include(":comparison-quiz")
include(":daily_challenge")
