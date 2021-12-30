object AppConfig {
    const val TEST_INSTRUMENTAL_RUNNER = "com.infinitepower.newquiz.compose.core.HiltTestRunner"
    const val COMPILE_SDK_VERSION = 31
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 31
    const val VERSION_CODE = 1
    const val VERSION_NAME = "0.0.2"
}

object Plugins {
    object Gradle {
        const val VERSION = "7.2.0-alpha03"

        const val APPLICATION = "com.android.application"
        const val LIBRARY = "com.android.library"
    }

    object Kotlin {
        const val VERSION = "1.6.10"

        const val KOTLIN_ANDROID = "org.jetbrains.kotlin.android"
        const val SERIALIZATION = "org.jetbrains.kotlin.plugin.serialization"
    }

    object Google {
        const val VERSION = "4.3.5"

        const val GOOGLE_SERVICES = "com.google.gms.google.services"
    }

    object Compose {
        private const val VERSION = "1.1.0-rc1"
    }

    object Coroutines {
        private const val VERSION = "1.6.0"
    }

    object Accompanist {
        private const val VERSION = "0.21.2-beta"
    }
}

object Dependencies