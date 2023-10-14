pluginManagement {
    includeBuild("build-logic")

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)


    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "NewQuiz"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")

include(":core")
include(":core:analytics")
include(":core:database")
include(":core:datastore")
include(":core:testing")
include(":core:translation")

include(":model")
include(":domain")
include(":data")
include(":multi-choice-quiz")
include(":settings-presentation")
include(":wordle")
include(":online_services")
include(":maze-quiz")
include(":comparison-quiz")
include(":daily_challenge")
