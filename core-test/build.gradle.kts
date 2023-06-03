import de.fayard.refreshVersions.core.versionFor

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

android {
    namespace = "com.infinitepower.newquiz.core_test"
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}

kotlin {
    jvmToolchain(ProjectConfig.jvmToolchainVersion)
}

dependencies {
    implementation(AndroidX.core.ktx)

    implementation(Testing.mockK.android)
    implementation(Testing.junit.jupiter)

    // Compose
    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.ui.toolingPreview)
    implementation(AndroidX.activity.compose)
    implementation(AndroidX.compose.material3)
    implementation(AndroidX.compose.material3.windowSizeClass)

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)
    kapt(AndroidX.hilt.compiler)
    implementation(AndroidX.hilt.navigationCompose)
    implementation(Google.dagger.hilt.android.testing)
    implementation(AndroidX.hilt.work)

    implementation(AndroidX.work.testing)

    implementation(libs.io.github.raamcosta.compose.destinations.core)
    ksp(libs.io.github.raamcosta.compose.destinations.ksp)

    // Google truth
    implementation(libs.truth)

    implementation(Kotlin.test.junit)

    implementation(AndroidX.test.runner)
    implementation(AndroidX.test.rules)
    implementation(AndroidX.test.ext.junit)
    implementation(AndroidX.test.runner)

    implementation(AndroidX.compose.ui.test)
    implementation(AndroidX.compose.ui.testJunit4)
    debugImplementation(AndroidX.compose.ui.testManifest)

    implementation(AndroidX.test.espresso.core)

    implementation(project(Modules.core))
    androidTestImplementation(project(Modules.comparisonQuiz))
    androidTestImplementation(project(Modules.model))
    androidTestImplementation(project(Modules.data))
    androidTestImplementation(project(Modules.domain))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
