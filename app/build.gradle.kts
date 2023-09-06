import com.infinitepower.newquiz.Modules
import com.infinitepower.newquiz.ProjectConfig

plugins {
    id("newquiz.android.application")
    id("newquiz.android.application.compose")
    id("newquiz.android.hilt")
    id("newquiz.android.compose.destinations")
    id("newquiz.kotlin.serialization")
    id("kotlin-parcelize")
    id("com.google.android.gms.oss-licenses-plugin")
}

android {
    namespace = "com.infinitepower.newquiz"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = "com.infinitepower.newquiz"
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = 14
        versionName = "1.6.1"

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
    implementation(libs.androidx.dataStore.preferences)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material)
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

    implementation(libs.androidx.navigation.compose)

    implementation(libs.lottie.compose)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.serialization)

    implementation(libs.androidx.work.ktx)
    androidTestImplementation(libs.androidx.work.testing)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remoteConfig.ktx)

    implementation(libs.kotlinx.datetime)

    implementation(libs.slf4j.simple)

    implementation(libs.google.oss.licenses)

    implementation(project(Modules.core))
    implementation(project(Modules.coreAnalytics))
    implementation(project(Modules.coreTranslation))
    implementation(project(Modules.model))
    implementation(project(Modules.multiChoicequiz))
    implementation(project(Modules.settingsPresentation))
    implementation(project(Modules.wordle))
    implementation(project(Modules.data))
    implementation(project(Modules.domain))
    implementation(project(Modules.onlineServices))
    implementation(project(Modules.mazeQuiz))
    implementation(project(Modules.comparisonQuiz))
    implementation(project(Modules.dailyChallenge))
}
