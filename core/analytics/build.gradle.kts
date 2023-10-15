plugins {
    alias(libs.plugins.newquiz.android.library.compose)
    alias(libs.plugins.newquiz.android.hilt)
}

android {
    namespace = "com.infinitepower.newquiz.core.analytics"
}

dependencies {
    implementation(libs.androidx.compose.runtime)

    "normalImplementation"(platform(libs.firebase.bom))
    "normalImplementation"(libs.firebase.analytics.ktx)
    "normalImplementation"(libs.firebase.crashlytics.ktx)
    "normalImplementation"(libs.firebase.perf.ktx)
}