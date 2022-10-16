import de.fayard.refreshVersions.core.versionFor

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp") version "1.7.20-1.0.6"
}

android {
    namespace = "com.infinitepower.newquiz.core"
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
    lint {
        disable += "DialogFragmentCallbacksDetector"
        disable += "UnusedResources"
    }
}

dependencies {
    testImplementation(Testing.junit.jupiter)
    testImplementation(libs.truth)

    implementation(AndroidX.core.ktx)

    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.ui.toolingPreview)
    implementation(AndroidX.activity.compose)
    implementation(AndroidX.compose.material)
    implementation(AndroidX.compose.material3)

    implementation(Google.android.material)

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)
    kapt(AndroidX.hilt.compiler)
    implementation(AndroidX.hilt.navigationCompose)
    androidTestImplementation(Google.dagger.hilt.android.testing)
    kaptAndroidTest(Google.dagger.hilt.compiler)
    implementation(AndroidX.hilt.work)

    implementation(AndroidX.work.runtimeKtx)

    implementation(Google.android.material)

    implementation(AndroidX.dataStore.preferences)

    api(Google.firebase.analyticsKtx.withVersionPlaceholder())

    implementation(libs.lottie.compose)

    implementation(KotlinX.datetime)

    implementation(libs.play.services.ads)

    implementation(KotlinX.serialization.json)

    implementation(libs.io.github.raamcosta.compose.destinations.core)
    ksp(libs.ksp)
}

tasks.withType<Test> {
    useJUnitPlatform()
}