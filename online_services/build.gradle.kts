import com.infinitepower.newquiz.Modules

plugins {
    id("newquiz.android.library.compose")
    id("newquiz.android.hilt")
    id("newquiz.android.compose.destinations")
    id("newquiz.kotlin.serialization")
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
    implementation(libs.firebase.remoteConfig.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)

    implementation(libs.firebaseUi.auth)

    implementation(libs.coil.kt.compose)

    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)

    implementation(project(Modules.core))
    implementation(project(Modules.coreAnalytics))
    implementation(project(Modules.model))
}
