import com.infinitepower.newquiz.Modules

plugins {
    id("newquiz.android.library")
    id("newquiz.android.hilt")
}

android {
    namespace = "com.infinitepower.newquiz.core.translation"
}

dependencies {
    implementation(project(Modules.model))
    implementation(project(Modules.core))

    "normalImplementation"(libs.google.mlKit.translate)

    implementation(libs.kotlinx.coroutines.playServices)

    implementation(libs.androidx.lifecycle.livedata.ktx)

    // WorkManager
    implementation(libs.androidx.work.ktx)

    // Hilt with WorkManager
    implementation(libs.hilt.ext.work)
    kapt(libs.hilt.ext.compiler)

    // Preferences DataStore
    implementation(libs.androidx.dataStore.preferences)
}