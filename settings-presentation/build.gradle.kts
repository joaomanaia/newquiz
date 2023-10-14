plugins {
    id("newquiz.android.library.compose")
    id("newquiz.android.hilt")
    id("newquiz.android.compose.destinations")
}

android {
    namespace = "com.infinitepower.newquiz.settings_presentation"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.material.iconsExtended)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    implementation(libs.hilt.navigationCompose)

    implementation(libs.google.oss.licenses)

    // WorkManager
    implementation(libs.androidx.work.ktx)

    // Hilt with WorkManager
    implementation(libs.hilt.ext.work)
    ksp(libs.hilt.ext.compiler)

    implementation(projects.core)
    implementation(projects.core.analytics)
    implementation(projects.core.datastore)
    implementation(projects.core.translation)
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.model)
    implementation(projects.onlineServices)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
