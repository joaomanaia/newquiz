plugins {
    alias(libs.plugins.newquiz.android.feature)
    alias(libs.plugins.newquiz.android.compose.destinations)
    alias(libs.plugins.newquiz.detekt)
}

android {
    namespace = "com.infinitepower.newquiz.feature.daily_challenge"
}

dependencies {
    implementation(libs.kotlinx.datetime)

    implementation(projects.core.userServices)
    implementation(projects.domain)
    implementation(projects.data)
}
