plugins {
    alias(libs.plugins.newquiz.android.library.compose)
    alias(libs.plugins.newquiz.android.hilt)
}

android {
    namespace = "com.infinitepower.newquiz.core.testing"
}

dependencies {
    api(libs.androidx.activity.compose)
    api(libs.androidx.compose.ui.test)
    api(libs.androidx.test.rules)
    api(libs.androidx.test.runner)
    api(libs.hilt.android.testing)
    api(libs.kotlinx.coroutines.test)
    api(libs.androidx.compose.material3)

    api(libs.room.testing)
    api(libs.androidx.work.testing)

    debugApi(libs.androidx.compose.ui.testManifest)

    implementation(projects.model)
    implementation(projects.core)
    implementation(projects.core.database)
    implementation(projects.core.remoteConfig)
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.onlineServices)
}