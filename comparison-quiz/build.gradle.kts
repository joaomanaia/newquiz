plugins {
    alias(libs.plugins.newquiz.android.library.compose)
    alias(libs.plugins.newquiz.android.hilt)
    alias(libs.plugins.newquiz.android.compose.destinations)
    alias(libs.plugins.newquiz.kotlin.serialization)
}

android {
    namespace = "com.infinitepower.newquiz.comparison_quiz"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.material.iconsExtended)
    debugImplementation(libs.androidx.compose.ui.testManifest)
    implementation(libs.androidx.constraintlayout.compose)

    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)

    implementation(libs.hilt.navigationCompose)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.svg)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.serialization)

    implementation(libs.androidx.work.ktx)
    androidTestImplementation(libs.androidx.work.testing)

    implementation(libs.lottie.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remoteConfig)

    implementation(projects.core)
    implementation(projects.core.analytics)
    implementation(projects.model)
    implementation(projects.domain)
    implementation(projects.data)
    implementation(projects.onlineServices)
    androidTestImplementation(projects.core.testing)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "comparison-quiz")
}