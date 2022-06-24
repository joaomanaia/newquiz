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
        kotlinCompilerExtensionVersion = Compose.composeVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        baseline = file("lint-baseline.xml")
    }
    namespace = "com.infinitepower.newquiz.compose"
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
    implementation(AndroidX.lifecycle.liveDataKtx)
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
    testImplementation("com.google.truth:truth:_")
    testImplementation(Kotlin.test.junit)
    androidTestImplementation(Kotlin.test.junit)

    implementation(Google.android.material)
    implementation(AndroidX.compose.material3)
    implementation(AndroidX.compose.material3.windowSizeClass)
    implementation(AndroidX.compose.material)

    implementation(AndroidX.lifecycle.viewModelCompose)

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.android.compiler)
    kapt(AndroidX.hilt.compiler)
    implementation(AndroidX.hilt.navigationCompose)
    androidTestImplementation(Google.dagger.hilt.android.testing)
    kaptAndroidTest(Google.dagger.hilt.android.compiler)
    implementation(AndroidX.hilt.work)

    implementation(KotlinX.coroutines.android)
    implementation(KotlinX.coroutines.test)
    implementation(KotlinX.coroutines.playServices)

    implementation(COIL.compose)

    implementation(platform(Firebase.bom))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // FirebaseUI for Firebase Auth
    implementation("com.firebaseui:firebase-ui-auth:_")

    implementation(AndroidX.navigation.compose)

    implementation("com.facebook.android:facebook-login:_")

    implementation("com.airbnb.android:lottie-compose:_")

    implementation("com.google.android.gms:play-services-ads:_")

    implementation(Ktor.client.core)
    implementation(Ktor.client.okHttp)
    implementation(Ktor.client.serialization)

    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation(Square.leakCanary.android)

    implementation(AndroidX.work.runtimeKtx)
    androidTestImplementation(AndroidX.work.testing)

    val roomVersion = "2.4.2"

    implementation(AndroidX.room.runtime)
    annotationProcessor(AndroidX.room.compiler)
    ksp(AndroidX.room.compiler)
    implementation(AndroidX.room.ktx)
    testImplementation(AndroidX.room.testing)
    implementation(AndroidX.room.paging)

    testImplementation("com.google.truth:truth:_")
    androidTestImplementation("com.google.truth:truth:_")

    implementation(AndroidX.paging.runtime)
    implementation(AndroidX.paging.compose)

    implementation("io.github.serpro69:kotlin-faker:_")

    implementation("io.github.raamcosta.compose-destinations:core:1.6.12-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.6.12-beta")

    implementation(project(Modules.core))
    implementation(project(Modules.model))
    implementation(project(Modules.homePresentation))
    implementation(project(Modules.quizPresentation))
}

tasks.withType<Test> {
    useJUnitPlatform()
}