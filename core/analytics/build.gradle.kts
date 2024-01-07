plugins {
    alias(libs.plugins.newquiz.android.library.compose)
    alias(libs.plugins.newquiz.android.hilt)
    id("newquiz.detekt")
}

android {
    namespace = "com.infinitepower.newquiz.core.analytics"
}

dependencies {
    implementation(libs.androidx.compose.runtime)

    normalImplementation(platform(libs.firebase.bom))
    normalImplementation(libs.firebase.analytics)
    normalImplementation(libs.firebase.crashlytics)
    normalImplementation(libs.firebase.perf)

    normalImplementation(projects.model)
    normalImplementation(projects.core.datastore)
}