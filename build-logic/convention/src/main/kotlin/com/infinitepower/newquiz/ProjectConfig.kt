package com.infinitepower.newquiz

import org.gradle.api.JavaVersion

object ProjectConfig {
    const val compileSdk = 34

    const val minSdk = 21

    const val targetSdk = 34

    val javaVersionCompatibility = JavaVersion.VERSION_17

    const val jvmTargetVersion = "17"

    const val jvmToolchainVersion = 17
}