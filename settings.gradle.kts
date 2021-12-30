pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "dagger.hilt.android.plugin" -> useModule("com.google.dagger:hilt-android-gradle-plugin:2.39.1")
                "com.google.gms.google.services" -> useModule("com.google.gms:google-services:4.3.5")
                "com.google.firebase.firebase-perf" -> useModule("com.google.firebase:perf-plugin:1.4.0")
                "com.google.firebase.crashlytics" -> useModule("com.google.firebase:firebase-crashlytics-gradle:2.8.0")
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
rootProject.name = "NewQuiz"
include(":app")
