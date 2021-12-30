plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

android {
    compileSdk = AppConfig.COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = "com.infinitepower.newquiz.compose"
        minSdk = AppConfig.MIN_SDK_VERSION
        targetSdk = AppConfig.TARGET_SDK_VERSION
        versionCode = AppConfig.VERSION_CODE
        versionName = AppConfig.VERSION_NAME

        testInstrumentationRunner = AppConfig.TEST_INSTRUMENTAL_RUNNER
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-rc02"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.infinitepower.newquiz.compose"
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.activity:activity-ktx:1.4.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-rc02")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.6.10")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.6.10")

    implementation("androidx.compose.ui:ui:1.1.0-rc01")
    implementation("androidx.compose.ui:ui-tooling:1.1.0-rc01")
    implementation("androidx.activity:activity-compose:1.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.0-rc01")

    implementation("com.google.android.material:material:1.6.0-alpha01")
    implementation("androidx.compose.material3:material3:1.0.0-alpha02")
    implementation("androidx.compose.material:material:1.1.0-rc01")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0")

    implementation("com.google.dagger:hilt-android:2.40.5")
    kapt("com.google.dagger:hilt-android-compiler:2.40.5")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0-rc01")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.40.5")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.40.5")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.0")

    implementation("io.coil-kt:coil-compose:2.0.0-alpha05")

    implementation(platform("com.google.firebase:firebase-bom:29.0.3"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    //implementation("com.google.mlkit:translate:16.1.2")

    // FirebaseUI for Firebase Auth
    implementation("com.firebaseui:firebase-ui-auth:8.0.0")

    implementation("androidx.navigation:navigation-compose:2.4.0-rc01")

    implementation("com.facebook.android:facebook-login:12.2.0")

    implementation("com.airbnb.android:lottie-compose:4.2.2")

    implementation("com.google.android.gms:play-services-ads:20.5.0")

    implementation("io.ktor:ktor-client-core:1.6.7")
    implementation("io.ktor:ktor-client-android:1.6.7")
    implementation("io.ktor:ktor-client-serialization:1.6.7")

    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.7")
}

tasks.withType<Test> {
    useJUnitPlatform()
}