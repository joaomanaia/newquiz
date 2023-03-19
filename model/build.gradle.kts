import de.fayard.refreshVersions.core.versionFor

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

android {
    namespace = "com.infinitepower.newquiz.model"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk

        testInstrumentationRunner = ProjectConfig.testInstrumentationRunner
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
        sourceCompatibility = ProjectConfig.javaVersionCompatibility
        targetCompatibility = ProjectConfig.javaVersionCompatibility
    }
    kotlinOptions {
        jvmTarget = ProjectConfig.jvmTargetVersion
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
}

dependencies {
    testImplementation(Testing.junit.jupiter)
    testImplementation(libs.truth)
    testImplementation(Testing.mockK.android)

    implementation(AndroidX.annotation)

    implementation(KotlinX.serialization.json)

    implementation(KotlinX.datetime)

    implementation(AndroidX.room.runtime)
    ksp(AndroidX.room.compiler)
    implementation(AndroidX.room.ktx)

    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.ui.tooling)
}

tasks.withType<Test> {
    useJUnitPlatform()
}