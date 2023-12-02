plugins {
    alias(libs.plugins.newquiz.android.library.compose)
    alias(libs.plugins.newquiz.android.hilt)
    alias(libs.plugins.newquiz.android.compose.destinations)
}

android {
    namespace = "com.infinitepower.newquiz.core.user_services.ui"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    implementation(libs.kotlinx.datetime)

    implementation(libs.coil.kt.compose)

    implementation(libs.hilt.navigationCompose)

    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)

    implementation(projects.core)
    implementation(projects.core.userServices)
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "user-services-ui")
}
