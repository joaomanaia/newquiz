plugins {
    id("newquiz.android.library.compose")
    id("newquiz.android.hilt")
}

android {
    namespace = "com.infinitepower.newquiz.core.analytics"
}

dependencies {
    implementation(libs.androidx.compose.runtime)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)
}