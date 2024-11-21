plugins {
    alias(libs.plugins.newquiz.android.library)
    alias(libs.plugins.newquiz.android.room)
    alias(libs.plugins.newquiz.android.hilt)
    alias(libs.plugins.newquiz.kotlin.serialization)
    alias(libs.plugins.newquiz.detekt)
}

android {
    namespace = "com.infinitepower.newquiz.core.database"
}

dependencies {
    implementation(libs.kotlinx.datetime)

    implementation(projects.model)

    androidTestImplementation(projects.core.testing)
}
