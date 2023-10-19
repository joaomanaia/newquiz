plugins {
    alias(libs.plugins.newquiz.android.library.compose)
    alias(libs.plugins.newquiz.android.hilt)
    alias(libs.plugins.newquiz.android.compose.destinations)
    alias(libs.plugins.newquiz.kotlin.serialization)
}

android {
    namespace = "com.infinitepower.newquiz.online_services"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtimeCompose)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.playServices)

    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.material.iconsExtended)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    implementation(libs.hilt.navigationCompose)
    ksp(libs.hilt.ext.compiler)
    implementation(libs.hilt.ext.work)

    implementation(libs.androidx.work.ktx)
    androidTestImplementation(libs.androidx.work.testing)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remoteConfig)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    implementation(libs.firebaseUi.auth)

    implementation(libs.coil.kt.compose)

    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)

    implementation(projects.core)
    implementation(projects.core.analytics)
    implementation(projects.model)
}
