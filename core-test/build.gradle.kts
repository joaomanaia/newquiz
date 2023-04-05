import de.fayard.refreshVersions.core.versionFor

plugins {
    id("com.android.library")
    kotlin("android")
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
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.compiler)
    }
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

    // Google truth
    implementation(libs.truth)

    implementation(Kotlin.test.junit)

    implementation(AndroidX.test.runner)
    implementation(AndroidX.test.rules)
    implementation(AndroidX.test.ext.junit)
    implementation(AndroidX.test.runner)

    implementation(AndroidX.compose.ui.test)
    implementation(AndroidX.compose.ui.testJunit4)

    implementation(AndroidX.test.espresso.core)

    implementation(project(Modules.core))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
