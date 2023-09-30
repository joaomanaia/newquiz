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
    implementation(project(Modules.coreDatastore))

    "normalImplementation"(libs.google.mlKit.translate)
    "normalImplementation"(libs.kotlinx.coroutines.playServices)
}