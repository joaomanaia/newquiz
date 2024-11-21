plugins {
    alias(libs.plugins.newquiz.android.feature)
    alias(libs.plugins.newquiz.android.compose.destinations)
    alias(libs.plugins.newquiz.detekt)
}

android {
    namespace = "com.infinitepower.newquiz.feature.profile"
}

dependencies {
    implementation(libs.androidx.lifecycle.livedata.ktx)

    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)

    implementation(libs.kotlinx.datetime)

    implementation(libs.coil.kt.compose)

    implementation(projects.core)
    implementation(projects.core.userServices)
}
