import com.infinitepower.newquiz.Modules

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
    kapt(libs.hilt.ext.compiler)
    implementation(libs.hilt.ext.work)

    implementation(libs.androidx.dataStore.preferences)

    implementation(libs.androidx.work.ktx)
    androidTestImplementation(libs.androidx.work.testing)

    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remoteConfig.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)

    implementation(libs.kotlinx.datetime)

    implementation(project(Modules.core))
    implementation(project(Modules.coreAnalytics))
    implementation(project(Modules.domain))
    implementation(project(Modules.model))
    androidTestImplementation(project(Modules.coreTest))
}

ksp {
    arg("room.schemaLocation", "$rootDir/app/schemas")
    arg("room.incremental", "true")
}
