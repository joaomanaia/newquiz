plugins {
    alias(libs.plugins.newquiz.android.feature)
    alias(libs.plugins.newquiz.android.compose.destinations)
}

android {
    namespace = "com.infinitepower.newquiz.feature.maze"
}

dependencies {
    implementation(libs.androidx.work.ktx)

    implementation(libs.androidx.lifecycle.livedata.ktx)

    implementation(libs.androidx.compose.material.iconsExtended)

    implementation(projects.data)
    implementation(projects.domain)
}
