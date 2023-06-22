import de.fayard.refreshVersions.core.versionFor

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.infinitepower.newquiz.settings_presentation"
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
    }
}

kotlin {
    jvmToolchain(ProjectConfig.jvmToolchainVersion)
}

dependencies {
    implementation(AndroidX.core.ktx)

    implementation(AndroidX.lifecycle.runtime.ktx)
    implementation(AndroidX.lifecycle.runtime.compose)

    testImplementation(Testing.junit.jupiter)
    testImplementation(libs.truth)
    androidTestImplementation(Kotlin.test.junit)

    implementation(Google.android.material)

    debugImplementation(AndroidX.compose.ui.testManifest)
    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.ui.toolingPreview)
    implementation(AndroidX.activity.compose)
    implementation(AndroidX.compose.material3)
    implementation(AndroidX.compose.material3.windowSizeClass)
    implementation(AndroidX.compose.material.icons.extended)

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)
    kapt(AndroidX.hilt.compiler)
    implementation(AndroidX.hilt.navigationCompose)
    androidTestImplementation(Google.dagger.hilt.android.testing)
    kaptAndroidTest(Google.dagger.hilt.compiler)

    implementation(AndroidX.dataStore.preferences)

    implementation(libs.io.github.raamcosta.compose.destinations.core)
    ksp(libs.io.github.raamcosta.compose.destinations.ksp)

    implementation(libs.play.services.oss.licenses)

    implementation(project(Modules.core))
    implementation(project(Modules.data))
    implementation(project(Modules.domain))
    implementation(project(Modules.model))
    implementation(project(Modules.translationDynamicFeature))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "settings")
}