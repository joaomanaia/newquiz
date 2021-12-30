plugins {
    id(Plugins.Gradle.APPLICATION) version Plugins.Gradle.VERSION apply false
    id(Plugins.Gradle.LIBRARY) version Plugins.Gradle.VERSION apply false
    id(Plugins.Kotlin.KOTLIN_ANDROID) version Plugins.Kotlin.VERSION apply false
    id(Plugins.Kotlin.SERIALIZATION) version Plugins.Kotlin.VERSION apply false
    id(Plugins.Google.GOOGLE_SERVICES) version Plugins.Google.VERSION apply false
}

tasks.register("clean", Delete::class) {
    delete (rootProject.buildDir)
}