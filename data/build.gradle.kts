plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp") version "1.6.21-1.0.5"
}

android {
    namespace = "com.infinitepower.newquiz.data"
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
}

dependencies {
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.appCompat)
    implementation(Google.android.material)
    testImplementation(Testing.junit4)
    androidTestImplementation(AndroidX.test.ext.junit)
    androidTestImplementation(AndroidX.test.espresso.core)

    implementation(platform(Firebase.bom))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    implementation(KotlinX.coroutines.android)
    implementation(KotlinX.coroutines.playServices)

    implementation(Ktor.client.core)
    implementation(Ktor.client.okHttp)
    implementation(Ktor.client.serialization)

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.android.compiler)
    kapt(AndroidX.hilt.compiler)
    implementation(AndroidX.hilt.navigationCompose)
    androidTestImplementation(Google.dagger.hilt.android.testing)
    kaptAndroidTest(Google.dagger.hilt.android.compiler)
    implementation(AndroidX.hilt.work)

    implementation(AndroidX.dataStore.preferences)

    implementation(AndroidX.work.runtimeKtx)

    implementation(AndroidX.room.runtime)
    annotationProcessor(AndroidX.room.compiler)
    ksp(AndroidX.room.compiler)
    implementation(AndroidX.room.ktx)

    implementation(Google.firebase.remoteConfigKtx.withVersionPlaceholder())

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":model"))
}