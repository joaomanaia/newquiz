plugins {
    alias(libs.plugins.newquiz.android.feature)
    alias(libs.plugins.newquiz.android.compose.destinations)
    alias(libs.plugins.newquiz.detekt)
}

android {
    namespace = "com.infinitepower.newquiz.feature.maze"
}

dependencies {
    implementation(libs.androidx.work.ktx)

    implementation(libs.androidx.lifecycle.livedata.ktx)

    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.graphics.shapes)

    implementation(libs.lottie.compose)

    implementation(projects.core.datastore)
    implementation(projects.data)
    implementation(projects.domain)
    testImplementation(projects.core.testing)
}
