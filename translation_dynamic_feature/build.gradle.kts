import com.infinitepower.newquiz.Modules

plugins {
    id("newquiz.android.library")
    id("newquiz.android.hilt")
}

android {
    namespace = "com.infinitepower.newquiz.translation_dynamic_feature"
}

dependencies {
    implementation(project(Modules.model))

    implementation(libs.google.mlKit.translate)

    implementation(libs.kotlinx.coroutines.playServices)
}