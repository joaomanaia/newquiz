import de.fayard.refreshVersions.core.versionFor

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}
android {
    namespace = "com.infinitepower.newquiz.translation_dynamic_feature"
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
    }
    lint {
        disable += "DialogFragmentCallbacksDetector"
    }
}

dependencies {
    implementation(project(Modules.core))

    implementation(AndroidX.core.ktx)
    implementation("androidx.core:core-ktx:+")
    testImplementation(Testing.junit4)
    androidTestImplementation(AndroidX.test.ext.junit)
    androidTestImplementation(AndroidX.test.espresso.core)
    androidTestImplementation(AndroidX.annotation)

    implementation(Google.mlKit.naturalLanguage.translate)

    implementation(KotlinX.Coroutines.playServices)

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)
    kapt(AndroidX.hilt.compiler)
    implementation(AndroidX.hilt.navigationCompose)
    androidTestImplementation(Google.dagger.hilt.android.testing)
    kaptAndroidTest(Google.dagger.hilt.compiler)
}