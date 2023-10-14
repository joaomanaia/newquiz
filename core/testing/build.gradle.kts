import com.infinitepower.newquiz.Modules

plugins {
    id("newquiz.android.library.compose")
    id("newquiz.android.hilt")
}

android {
    namespace = "com.infinitepower.newquiz.core.testing"
}

dependencies {
    api(libs.androidx.activity.compose)
    api(libs.androidx.compose.ui.test)
    api(libs.androidx.test.rules)
    api(libs.androidx.test.runner)
    api(libs.hilt.android.testing)
    api(libs.kotlinx.coroutines.test)
    api(libs.androidx.compose.material3)

    api(libs.room.testing)
    api(libs.androidx.work.testing)

    debugApi(libs.androidx.compose.ui.testManifest)

    implementation(project(Modules.model))
    implementation(project(Modules.core))
    implementation(project(Modules.data))
    implementation(project(Modules.domain))
    implementation(project(Modules.coreDatabase))
    implementation(project(Modules.onlineServices))
}