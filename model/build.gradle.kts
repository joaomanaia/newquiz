plugins {
    alias(libs.plugins.newquiz.jvm.library)
    alias(libs.plugins.newquiz.kotlin.serialization)
}

dependencies {
    testImplementation(libs.google.truth)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockk)

    implementation(libs.androidx.annotation)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)

    api(libs.kotlinx.collections.immutable)
}

tasks.withType<Test> {
    useJUnitPlatform()
}