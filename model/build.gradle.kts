plugins {
    kotlin("jvm")
    id("kotlinx-serialization")
}

kotlin {
    jvmToolchain(ProjectConfig.jvmToolchainVersion)
}

dependencies {
    testImplementation(Testing.junit.jupiter)
    testImplementation(libs.truth)
    testImplementation(Testing.mockK)

    implementation(AndroidX.annotation)

    implementation(KotlinX.serialization.json)
    implementation(KotlinX.coroutines.core)
    implementation(KotlinX.datetime)
}

tasks.withType<Test> {
    useJUnitPlatform()
}