import de.fayard.refreshVersions.core.versionFor

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("com.google.devtools.ksp") version "1.7.21-1.0.8"
}

android {
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.applicationId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = ProjectConfig.testInstrumentationRunner
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            extra["enableCrashlytics"] = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.compiler)
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        disable += "DialogFragmentCallbacksDetector"
        baseline = file("lint-baseline.xml")
    }
    namespace = "com.infinitepower.newquiz"

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }

        addJavaSourceFoldersToModel(File(buildDir, "generated/ksp/${name}/kotlin"))
    }
}

dependencies {
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.activity.ktx)
    implementation(AndroidX.lifecycle.runtime.ktx)
    implementation(AndroidX.constraintLayout.compose)
    implementation(AndroidX.core.splashscreen)

    androidTestImplementation(AndroidX.test.ext.junit)
    androidTestImplementation(AndroidX.test.espresso.core)

    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.material.icons.extended)
    implementation(AndroidX.activity.compose)
    androidTestImplementation(AndroidX.compose.ui.testJunit4)

    testImplementation(Testing.junit4)
    testImplementation(Testing.junit.jupiter)
    testImplementation(Kotlin.test.junit)
    androidTestImplementation(Kotlin.test.junit)

    implementation(Google.android.material)
    implementation(AndroidX.compose.material3)
    implementation(AndroidX.compose.material3.windowSizeClass)
    implementation(AndroidX.compose.material)

    implementation(AndroidX.lifecycle.viewModelCompose)

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)
    kapt(AndroidX.hilt.compiler)
    implementation(AndroidX.hilt.navigationCompose)
    androidTestImplementation(Google.dagger.hilt.android.testing)
    kaptAndroidTest(Google.dagger.hilt.compiler)
    implementation(AndroidX.hilt.work)

    testImplementation(KotlinX.coroutines.test)
    implementation(KotlinX.coroutines.playServices)

    implementation(AndroidX.navigation.compose)

    implementation(libs.lottie.compose)

    implementation(Ktor.client.core)
    implementation(Ktor.client.okHttp)
    implementation(Ktor.client.serialization)

    //debugImplementation(Square.leakCanary.android)

    implementation(AndroidX.work.runtimeKtx)
    androidTestImplementation(AndroidX.work.testing)

    testImplementation(libs.truth)
    androidTestImplementation(libs.truth)

    implementation(libs.io.github.raamcosta.compose.destinations.core)
    ksp(libs.io.github.raamcosta.compose.destinations.ksp)

    implementation(Google.firebase.analyticsKtx.withVersionPlaceholder())
    implementation(Google.firebase.remoteConfigKtx.withVersionPlaceholder())
    implementation(Google.firebase.crashlyticsKtx.withVersionPlaceholder())
    implementation(Google.firebase.performanceMonitoringKtx.withVersionPlaceholder())
    implementation(libs.firebase.appcheck.safetynet)

    implementation(KotlinX.datetime)

    implementation(libs.play.services.ads)
    implementation(libs.user.messaging.platform)

    implementation(libs.slf4j.simple)

    implementation(project(Modules.core))
    implementation(project(Modules.model))
    implementation(project(Modules.homePresentation))
    implementation(project(Modules.multiChoicequiz))
    implementation(project(Modules.settingsPresentation))
    implementation(project(Modules.wordle))
    implementation(project(Modules.data))
    implementation(project(Modules.domain))
    implementation(project(Modules.onlineServices))
    implementation(project(Modules.mathQuiz))
    implementation(project(Modules.mazeQuiz))
}

tasks.withType<Test> {
    useJUnitPlatform()
}