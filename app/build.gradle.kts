plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp") version "1.6.21-1.0.6"
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

        androidResources {
            noCompress("tflite")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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
        kotlinCompilerExtensionVersion = Compose.composeCompilerVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        baseline = file("lint-baseline.xml")
    }
    namespace = "com.infinitepower.newquiz"
}

kotlin {
    sourceSets {
        debug {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        release {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}

dependencies {
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.activity.ktx)
    implementation(AndroidX.lifecycle.runtimeKtx)
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

    implementation("com.airbnb.android:lottie-compose:_")

    implementation(Ktor.client.core)
    implementation(Ktor.client.okHttp)
    implementation(Ktor.client.serialization)

    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation(Square.leakCanary.android)

    implementation(AndroidX.work.runtimeKtx)
    androidTestImplementation(AndroidX.work.testing)

    testImplementation("com.google.truth:truth:_")
    androidTestImplementation("com.google.truth:truth:_")

    implementation("io.github.raamcosta.compose-destinations:core:_")
    ksp("io.github.raamcosta.compose-destinations:ksp:_")

    implementation(Google.firebase.analyticsKtx.withVersionPlaceholder())
    implementation(Google.firebase.remoteConfigKtx.withVersionPlaceholder())

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    implementation("com.google.android.gms:play-services-ads:21.1.0")
    implementation("com.google.android.ump:user-messaging-platform:2.0.0")

    implementation(project(Modules.core))
    implementation(project(Modules.model))
    implementation(project(Modules.homePresentation))
    implementation(project(Modules.quizPresentation))
    implementation(project(Modules.settingsPresentation))
    implementation(project(Modules.wordle))
    implementation(project(Modules.data))
    implementation(project(Modules.domain))
}

tasks.withType<Test> {
    useJUnitPlatform()
}