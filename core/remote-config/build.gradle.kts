plugins {
    id("newquiz.android.library")
    alias(libs.plugins.newquiz.android.hilt)
    alias(libs.plugins.newquiz.kotlin.serialization)
    id("newquiz.detekt")
}

android {
    namespace = "com.infinitepower.newquiz.core.remote_config"
}

dependencies {
    implementation(libs.androidx.startup.runtime)

    normalImplementation(platform(libs.firebase.bom))
    normalImplementation(libs.firebase.remoteConfig)

    implementation(projects.model)
    normalImplementation(projects.core)
}
