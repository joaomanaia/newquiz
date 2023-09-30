import com.infinitepower.newquiz.Modules

plugins {
    id("newquiz.android.library.compose")
    id("newquiz.android.hilt")
    id("newquiz.android.compose.destinations")
    id("newquiz.kotlin.serialization")
}

android {
    namespace = "com.infinitepower.newquiz.core"

    lint {
        disable += "MissingTranslation"
    }
}

dependencies {
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugApi(libs.androidx.tracing.ktx)

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.constraintlayout.compose)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    implementation(libs.hilt.navigationCompose)
    implementation(libs.hilt.ext.work)
    ksp(libs.hilt.ext.compiler)

    implementation(libs.androidx.work.ktx)

    implementation(libs.google.material)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remoteConfig.ktx)

    implementation(libs.lottie.compose)

    implementation(libs.kotlinx.datetime)
    testImplementation(libs.kotlinx.coroutines.test)

    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.svg)

    //implementation("androidx.palette:palette-ktx:_")

    // Modules
    implementation(project(Modules.model))
    testImplementation(project(Modules.coreTest))
    androidTestImplementation(project(Modules.coreTest))
}
