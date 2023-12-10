plugins {
    alias(libs.plugins.newquiz.android.library.compose)
    alias(libs.plugins.newquiz.android.hilt)
    alias(libs.plugins.newquiz.android.compose.destinations)
}

android {
    namespace = "com.infinitepower.newquiz.wordle"
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

    // Hilt navigation compose utils
    implementation(libs.hilt.navigationCompose)
    // Hilt work manager
    implementation(libs.hilt.ext.work)
    ksp(libs.hilt.ext.compiler)

    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.work.ktx)
    androidTestImplementation(libs.androidx.work.testing)

    implementation(projects.core)
    implementation(projects.core.analytics)
    implementation(projects.core.userServices)
    implementation(projects.model)
    implementation(projects.domain)
    implementation(projects.data)
    androidTestImplementation(projects.core.testing)
}
