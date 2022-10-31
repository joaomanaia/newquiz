import de.fayard.refreshVersions.core.versionFor

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp") version "1.7.20-1.0.6"
}

android {
    namespace = "com.infinitepower.newquiz.home_presentation"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk

        testInstrumentationRunner = ProjectConfig.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.compiler)
    }
    libraryVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.lifecycle.runtime.ktx)

    testImplementation(Testing.junit.jupiter)
    testImplementation(libs.truth)
    androidTestImplementation(Kotlin.test.junit)

    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.ui.toolingPreview)
    implementation(AndroidX.activity.compose)
    implementation(AndroidX.compose.material.icons.extended)
    implementation(AndroidX.compose.material3)
    implementation(AndroidX.activity.compose)
    androidTestImplementation(AndroidX.compose.ui.testJunit4)

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)
    kapt(AndroidX.hilt.compiler)
    implementation(AndroidX.hilt.navigationCompose)
    androidTestImplementation(Google.dagger.hilt.android.testing)
    kaptAndroidTest(Google.dagger.hilt.compiler)

    implementation(libs.io.github.raamcosta.compose.destinations.core)
    ksp(libs.ksp)

    implementation(libs.lottie.compose)
    implementation(AndroidX.dataStore.preferences)

    implementation(libs.firebase.ui.auth)

    implementation(platform(Firebase.bom))
    implementation(Firebase.remoteConfigKtx)

    implementation(project(Modules.core))
    implementation(project(Modules.model))
    implementation(project(Modules.data))
    implementation(project(Modules.domain))
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "home")
}