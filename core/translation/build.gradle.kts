plugins {
    id("newquiz.android.library")
    id("newquiz.android.hilt")
}

android {
    namespace = "com.infinitepower.newquiz.core.translation"
}

dependencies {
    implementation(projects.model)
    implementation(projects.core)
    implementation(projects.core.datastore)

    "normalImplementation"(libs.google.mlKit.translate)
    "normalImplementation"(libs.kotlinx.coroutines.playServices)
}