plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(ProjectConfig.jvmToolchainVersion)
}

dependencies {
    implementation(project(Modules.model))

    implementation(libs.javax.inject)
    implementation(AndroidX.annotation)
    implementation(KotlinX.coroutines.core)

}

tasks.withType<Test> {
    useJUnitPlatform()
}
