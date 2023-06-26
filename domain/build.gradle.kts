plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(ProjectConfig.jvmToolchainVersion)
}

dependencies {
    implementation(project(Modules.model))

    implementation(libs.javax.inject)
    implementation(libs.androidx.annotation)
    implementation(libs.kotlinx.coroutines.core)

}

tasks.withType<Test> {
    useJUnitPlatform()
}
