import de.fayard.refreshVersions.core.versionFor

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
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
    libraryVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }

        addJavaSourceFoldersToModel(File(buildDir, "generated/ksp/${name}/kotlin"))
    }
    lint {
        disable += "DialogFragmentCallbacksDetector"
        disable += "UnusedResources"
        disable += "MissingTranslation"
    }
    packaging {
        resources {
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    testImplementation(Testing.junit.jupiter)
    testImplementation(libs.truth)
    testImplementation(Testing.mockK.android)

    androidTestImplementation(libs.truth)
    androidTestImplementation(Testing.mockK.android)
    androidTestImplementation(Kotlin.test.junit)
    androidTestImplementation(AndroidX.test.runner)
    androidTestImplementation(AndroidX.test.rules)
    androidTestImplementation(AndroidX.test.ext.junit)
    androidTestImplementation(Testing.junit.jupiter)
    androidTestImplementation(AndroidX.test.runner)

    androidTestImplementation(AndroidX.compose.ui.test)
    androidTestImplementation(AndroidX.compose.ui.testJunit4)

    debugImplementation(AndroidX.compose.ui.testManifest)

    implementation(AndroidX.core.ktx)

    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.ui.toolingPreview)
    implementation(AndroidX.activity.compose)
    implementation(AndroidX.compose.material)
    implementation(AndroidX.compose.material3)
    implementation(AndroidX.constraintLayout.compose)

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

    implementation(Google.firebase.analyticsKtx.withVersionPlaceholder())
    implementation(Google.firebase.performanceMonitoringKtx.withVersionPlaceholder())
    implementation(Google.firebase.remoteConfigKtx.withVersionPlaceholder())
    implementation(Google.firebase.crashlyticsKtx.withVersionPlaceholder())

    implementation(libs.lottie.compose)

    implementation(KotlinX.datetime)

    implementation(KotlinX.serialization.json)

    testImplementation(KotlinX.coroutines.test)

    implementation(COIL.compose)
    implementation(COIL.svg)

    //implementation("androidx.palette:palette-ktx:1.0.0")

    implementation(libs.io.github.raamcosta.compose.destinations.core)
    ksp(libs.io.github.raamcosta.compose.destinations.ksp)

    // Modules
    implementation(project(Modules.model))
    testImplementation(project(Modules.coreTest))
    androidTestImplementation(project(Modules.coreTest))
}

tasks.withType<Test> {
    useJUnitPlatform()
}