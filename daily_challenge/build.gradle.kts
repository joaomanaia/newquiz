plugins {
    alias(libs.plugins.newquiz.android.library.compose)
    alias(libs.plugins.newquiz.android.hilt)
    alias(libs.plugins.newquiz.android.compose.destinations)
    alias(libs.plugins.newquiz.kotlin.serialization)
}

android {
    namespace = "com.infinitepower.newquiz.daily_challenge"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.constraintlayout.compose)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.google.truth)
    androidTestImplementation(libs.androidx.compose.ui.test)

    implementation(libs.hilt.navigationCompose)

    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.work.ktx)

    implementation(projects.core)
    implementation(projects.core.userServices)
    implementation(projects.model)
    implementation(projects.domain)
    implementation(projects.data)
    androidTestImplementation(projects.core.testing)
}
