plugins {
    alias(libs.plugins.newquiz.android.library.compose)
    alias(libs.plugins.newquiz.android.hilt)
    id("newquiz.detekt")
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

    api(libs.ktor.client.mock)

    api(libs.room.testing)
    api(libs.androidx.work.testing)

    debugApi(libs.androidx.compose.ui.testManifest)

    implementation(projects.model)
    implementation(projects.core)
    implementation(projects.core.analytics)
    api(projects.core.database)
    api(projects.core.datastore)
    implementation(projects.core.remoteConfig)
    implementation(projects.data)
    implementation(projects.domain)
}