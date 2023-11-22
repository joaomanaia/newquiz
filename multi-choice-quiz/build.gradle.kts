plugins {
    alias(libs.plugins.newquiz.android.library.compose)
    alias(libs.plugins.newquiz.android.hilt)
    alias(libs.plugins.newquiz.android.compose.destinations)
    alias(libs.plugins.newquiz.kotlin.serialization)
}

android {
    namespace = "com.infinitepower.newquiz.multi_choice_quiz"
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
    androidTestImplementation(libs.androidx.work.testing)

    implementation(libs.hilt.navigationCompose)

    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.room.testing)

    implementation(libs.lottie.compose)

    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.svg)

    implementation(libs.androidx.work.ktx)

    implementation(projects.core)
    implementation(projects.core.analytics)
    implementation(projects.core.datastore)
    implementation(projects.core.translation)
    implementation(projects.core.userServices)
    implementation(projects.model)
    implementation(projects.domain)
    implementation(projects.data)
    androidTestImplementation(projects.core.testing)
}
