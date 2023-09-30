import com.infinitepower.newquiz.Modules

plugins {
    id("newquiz.android.library")
    id("newquiz.android.hilt")
}

android {
    namespace = "com.infinitepower.newquiz.core.datastore"
}

dependencies {
    api(libs.androidx.dataStore.preferences)

    implementation(project(Modules.model))
    implementation(project(Modules.core))
}
