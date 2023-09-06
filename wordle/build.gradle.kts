import com.infinitepower.newquiz.Modules

plugins {
    id("newquiz.android.library.compose")
    id("newquiz.android.hilt")
    id("newquiz.android.compose.destinations")
}

android {
    namespace = "com.infinitepower.newquiz.wordle"
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
    implementation(libs.androidx.constraintlayout.compose)

    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)

    // Hilt navigation compose utils
    implementation(libs.hilt.navigationCompose)
    // Hilt work manager
    implementation(libs.hilt.ext.work)
    ksp(libs.hilt.ext.compiler)

    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.work.ktx)
    androidTestImplementation(libs.androidx.work.testing)

    implementation(project(Modules.core))
    implementation(project(Modules.coreAnalytics))
    implementation(project(Modules.model))
    implementation(project(Modules.domain))
    implementation(project(Modules.data))
    implementation(project(Modules.onlineServices))
    androidTestImplementation(project(Modules.coreTest))
}
