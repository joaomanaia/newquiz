plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp") version "1.7.21-1.0.8"
}

android {
    namespace = "com.infinitepower.newquiz.data"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk

        testInstrumentationRunner = "com.infinitepower.newquiz.data.HiltTestRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
    lint {
        abortOnError = false
    }
}

dependencies {
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.appCompat)
    implementation(Google.android.material)

    androidTestImplementation(AndroidX.test.ext.junit)
    androidTestImplementation(AndroidX.test.espresso.core)
    androidTestImplementation(libs.truth)

    testImplementation(Testing.junit4)
    testImplementation(Testing.junit.jupiter)
    testImplementation(libs.truth)
    testImplementation(Testing.mockK.android)

    implementation(platform(Firebase.bom))
    implementation(Firebase.authenticationKtx)
    implementation(Firebase.cloudFirestoreKtx)

    implementation(KotlinX.coroutines.android)
    implementation(KotlinX.coroutines.playServices)
    testImplementation(KotlinX.coroutines.test)
    androidTestImplementation(KotlinX.coroutines.test)

    implementation(Ktor.client.core)
    implementation(Ktor.client.okHttp)
    implementation(Ktor.client.serialization)

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)
    kapt(AndroidX.hilt.compiler)
    implementation(AndroidX.hilt.navigationCompose)
    androidTestImplementation(Google.dagger.hilt.android.testing)
    kaptAndroidTest(Google.dagger.hilt.compiler)
    implementation(AndroidX.hilt.work)

    implementation(AndroidX.dataStore.preferences)

    implementation(AndroidX.work.runtimeKtx)
    androidTestImplementation(AndroidX.work.testing)

    implementation(AndroidX.room.runtime)
    annotationProcessor(AndroidX.room.compiler)
    ksp(AndroidX.room.compiler)
    implementation(AndroidX.room.ktx)

    implementation(Google.firebase.remoteConfigKtx.withVersionPlaceholder())
    implementation(Google.firebase.analyticsKtx.withVersionPlaceholder())
    implementation(Google.firebase.performanceMonitoringKtx.withVersionPlaceholder())

    implementation(KotlinX.datetime)

    implementation(project(Modules.core))
    implementation(project(Modules.domain))
    implementation(project(Modules.model))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

ksp {
    arg("room.schemaLocation", "$rootDir/app/schemas")
}
