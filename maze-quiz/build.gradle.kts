plugins {
    alias(libs.plugins.newquiz.android.library.compose)
    alias(libs.plugins.newquiz.android.hilt)
    alias(libs.plugins.newquiz.android.compose.destinations)
}

android {
    namespace = "com.infinitepower.newquiz.maze_quiz"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime.ktx)
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
    ksp(libs.hilt.ext.compiler)
    kspAndroidTest(libs.hilt.compiler)

    implementation(libs.androidx.work.ktx)

    implementation(projects.core)
    implementation(projects.core.analytics)
    implementation(projects.model)
    implementation(projects.data)
    implementation(projects.domain)

    implementation(projects.feature.maze)
}
