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
    id("de.fayard.refreshVersions") version "0.50.0"
}

rootProject.name = "NewQuiz"
include(":app")
include(":core")
include(":model")
include(":domain")
include(":data")
include(":home-presentation")
include(":multi-choice-quiz")
include(":settings-presentation")
include(":wordle")
include(":translation_dynamic_feature")
