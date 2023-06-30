plugins {
    id("newquiz.android.library.compose")
    id("newquiz.android.hilt")
}

android {
    namespace = "com.infinitepower.newquiz.core_test"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.mockk)
    implementation(libs.junit.jupiter)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    api(libs.hilt.android.testing)
    implementation(libs.hilt.navigationCompose)
    kapt(libs.hilt.ext.compiler)
    implementation(libs.hilt.ext.work)

    implementation(libs.androidx.work.ktx)

    implementation(libs.androidx.test.runner)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.compose.ui.test)

    // implementation(AndroidX.test.espresso.core)

    implementation(project(Modules.core))
    androidTestImplementation(project(Modules.comparisonQuiz))
    androidTestImplementation(project(Modules.model))
    androidTestImplementation(project(Modules.data))
    androidTestImplementation(project(Modules.domain))
}
