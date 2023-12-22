plugins {
    alias(libs.plugins.newquiz.android.feature)
    alias(libs.plugins.newquiz.android.compose.destinations)
}

android {
    namespace = "com.infinitepower.newquiz.feature.settings"
}

dependencies {
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.material.iconsExtended)

    implementation(libs.google.oss.licenses) {
        exclude(group = "androidx.appcompat")
    }

    implementation(projects.core.datastore)
    implementation(projects.domain)
    implementation(projects.data)
    normalImplementation(projects.core.translation)
}
