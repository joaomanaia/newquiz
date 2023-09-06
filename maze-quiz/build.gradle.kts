import com.infinitepower.newquiz.Modules

plugins {
    id("newquiz.android.library.compose")
    id("newquiz.android.hilt")
    id("newquiz.android.compose.destinations")
}

android {
    namespace = "com.infinitepower.newquiz.maze_quiz"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime.ktx)
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

    implementation(libs.hilt.navigationCompose)
    ksp(libs.hilt.ext.compiler)
    kspAndroidTest(libs.hilt.compiler)

    implementation(libs.androidx.work.ktx)

    implementation(project(Modules.core))
    implementation(project(Modules.coreAnalytics))
    implementation(project(Modules.model))
    implementation(project(Modules.data))
    implementation(project(Modules.domain))
}
