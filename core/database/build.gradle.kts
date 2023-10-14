plugins {
    id("newquiz.android.library")
    id("newquiz.android.hilt")
    id("newquiz.android.room")
    id("newquiz.kotlin.serialization")
}

android {
    namespace = "com.infinitepower.newquiz.core.database"
}

dependencies {
    implementation(libs.kotlinx.datetime)

    implementation(projects.model)
}