plugins {
    alias(libs.plugins.newquiz.jvm.library)
}

dependencies {
    implementation(projects.model)

    implementation(libs.javax.inject)
    implementation(libs.androidx.annotation)
    implementation(libs.kotlinx.coroutines.core)
}
