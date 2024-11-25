plugins {
    alias(libs.plugins.newquiz.android.application)
    alias(libs.plugins.newquiz.android.application.compose)
    alias(libs.plugins.newquiz.android.hilt)
    alias(libs.plugins.newquiz.android.compose.destinations)
    alias(libs.plugins.newquiz.kotlin.serialization)
    id("kotlin-parcelize")
    id("com.google.android.gms.oss-licenses-plugin")
    alias(libs.plugins.newquiz.detekt)
}

android {
    namespace = "com.infinitepower.newquiz"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.infinitepower.newquiz"
        minSdk = 21
        targetSdk = 35
        versionCode = 16
        versionName = "2.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        // resourceConfigurations += setOf("en", "pt", "fr", "es", "nb")
    }

    androidResources {
        generateLocaleConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        debug {
            extra["enableCrashlytics"] = false

            rootProject.ext.set("firebasePerformanceInstrumentationEnabled", "false")
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("x86", "x86_64", "arm64-v8a", "armeabi-v7a")

            // Generate universal APK
            isUniversalApk = true
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout.compose)

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)

    implementation(libs.androidx.startup.runtime)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    debugImplementation(libs.androidx.compose.ui.testManifest)
    androidTestImplementation(libs.androidx.compose.ui.test)

    implementation(libs.google.material)

    implementation(libs.hilt.navigationCompose)
    implementation(libs.hilt.ext.work)
    ksp(libs.hilt.ext.compiler)

    testImplementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlinx.coroutines.playServices)

    implementation(libs.lottie.compose)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.serialization)

    implementation(libs.androidx.work.ktx)
    androidTestImplementation(libs.androidx.work.testing)

    implementation(libs.kotlinx.datetime)

    implementation(libs.slf4j.simple)

    implementation(libs.google.oss.licenses)

    implementation(libs.kotlinx.collections.immutable)

    implementation(projects.core)
    implementation(projects.core.analytics)
    implementation(projects.core.datastore)
    implementation(projects.core.translation)
    implementation(projects.core.remoteConfig)
    implementation(projects.core.userServices)
    implementation(projects.feature.dailyChallenge)
    implementation(projects.feature.settings)
    implementation(projects.feature.maze)
    implementation(projects.feature.profile)
    implementation(projects.model)
    implementation(projects.multiChoiceQuiz)
    implementation(projects.wordle)
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.comparisonQuiz)
}
