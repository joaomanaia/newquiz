plugins {
    id("newquiz.android.library")
    id("newquiz.android.hilt")
    id("newquiz.kotlin.serialization")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.infinitepower.newquiz.data"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.kotlinx.coroutines.playServices)

    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.compose.ui.test)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.serialization)

    implementation(libs.hilt.navigationCompose)
    ksp(libs.hilt.ext.compiler)
    implementation(libs.hilt.ext.work)

    implementation(libs.androidx.work.ktx)
    androidTestImplementation(libs.androidx.work.testing)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remoteConfig.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)

    implementation(libs.kotlinx.datetime)

    implementation(projects.core)
    implementation(projects.core.analytics)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.domain)
    implementation(projects.model)
    implementation(projects.onlineServices)
    androidTestImplementation(projects.core.testing)
}
