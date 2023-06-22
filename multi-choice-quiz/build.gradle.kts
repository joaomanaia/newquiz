import de.fayard.refreshVersions.core.versionFor

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.infinitepower.newquiz.multi_choice_quiz"
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

    implementation(AndroidX.lifecycle.runtime.ktx)
    implementation(AndroidX.lifecycle.runtime.compose)

    testImplementation(Testing.junit.jupiter)
    testImplementation(libs.truth)
    androidTestImplementation(libs.truth)
    androidTestImplementation(Kotlin.test.junit)
    androidTestImplementation(Testing.mockK.android)
    androidTestImplementation(AndroidX.work.testing)

    debugImplementation(AndroidX.compose.ui.testManifest)
    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.ui.toolingPreview)
    implementation(AndroidX.activity.compose)
    implementation(AndroidX.compose.material3)
    implementation(AndroidX.compose.material3.windowSizeClass)
    implementation(AndroidX.compose.material.icons.extended)
    implementation(AndroidX.constraintLayout.compose)

    androidTestImplementation(AndroidX.compose.ui.testJunit4)
    androidTestImplementation(AndroidX.test.runner)
    androidTestImplementation(AndroidX.test.rules)

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)
    kapt(AndroidX.hilt.compiler)
    implementation(AndroidX.hilt.navigationCompose)
    androidTestImplementation(Google.dagger.hilt.android.testing)
    kaptAndroidTest(Google.dagger.hilt.compiler)

    implementation(libs.io.github.raamcosta.compose.destinations.core)
    ksp(libs.io.github.raamcosta.compose.destinations.ksp)

    implementation(KotlinX.serialization.json)

    implementation(AndroidX.room.runtime)
    annotationProcessor(AndroidX.room.compiler)
    ksp(AndroidX.room.compiler)
    implementation(AndroidX.room.ktx)
    testImplementation(AndroidX.room.testing)
    implementation(AndroidX.room.paging)

    implementation(libs.lottie.compose)

    implementation(COIL.compose)
    implementation(COIL.svg)

    implementation(AndroidX.work.runtimeKtx)

    implementation(project(Modules.core))
    implementation(project(Modules.model))
    implementation(project(Modules.domain))
    implementation(project(Modules.data))
    implementation(project(Modules.translationDynamicFeature))
    implementation(project(Modules.onlineServices))
    androidTestImplementation(project(Modules.coreTest))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "quiz")
}