import com.infinitepower.newquiz.Modules

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

    implementation(libs.androidx.dataStore.preferences)

    implementation(libs.google.oss.licenses)

    // WorkManager
    implementation(libs.androidx.work.ktx)

    // Hilt with WorkManager
    implementation(libs.hilt.ext.work)
    ksp(libs.hilt.ext.compiler)

    implementation(project(Modules.core))
    implementation(project(Modules.coreAnalytics))
    implementation(project(Modules.coreTranslation))
    implementation(project(Modules.data))
    implementation(project(Modules.domain))
    implementation(project(Modules.model))
    implementation(project(Modules.onlineServices))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
