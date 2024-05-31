import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    alias(libs.plugins.detekt)
}

group = "com.infinitepower.newquiz.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.firebase.performance.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
}

detekt {
    buildUponDefaultConfig = true
    config.from(file("../../detekt.yml"))
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "newquiz.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "newquiz.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "newquiz.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "newquiz.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "newquiz.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "newquiz.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidFirebase") {
            id = "newquiz.android.application.firebase"
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }
        register("jvmLibrary") {
            id = "newquiz.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("kotlinSerialization") {
            id = "newquiz.kotlin.serialization"
            implementationClass = "KotlinSerializationConventionPlugin"
        }
        register("androidComposeDestinations") {
            id = "newquiz.android.compose.destinations"
            implementationClass = "AndroidComposeDestinationsConventionPlugin"
        }
        register("androidFeature") {
            id = "newquiz.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("detekt") {
            id = "newquiz.detekt"
            implementationClass = "DetektConventionPlugin"
        }
    }
}