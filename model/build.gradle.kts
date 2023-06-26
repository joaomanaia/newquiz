plugins {
    kotlin("jvm")
    id("kotlinx-serialization")
}

kotlin {
    jvmToolchain(ProjectConfig.jvmToolchainVersion)
}

dependencies {
    testImplementation(libs.google.truth)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockk)

    implementation(libs.androidx.annotation)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
}

tasks.withType<Test> {
    useJUnitPlatform()
}