plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("com.google.devtools.ksp") version "1.6.21-1.0.6"
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
    testImplementation(Testing.junit.jupiter)
    testImplementation("com.google.truth:truth:_")

    implementation(AndroidX.annotation)

    implementation(KotlinX.serialization.json)

    implementation(AndroidX.room.runtime)
    ksp(AndroidX.room.compiler)
    implementation(AndroidX.room.ktx)
}

tasks.withType<Test> {
    useJUnitPlatform()
}