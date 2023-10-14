import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.infinitepower.newquiz.ProjectConfig
import com.infinitepower.newquiz.androidTestImplementation
import com.infinitepower.newquiz.configureFlavors
import com.infinitepower.newquiz.configureKotlinAndroid
import com.infinitepower.newquiz.disableUnnecessaryAndroidTests
import com.infinitepower.newquiz.libs
import com.infinitepower.newquiz.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureFlavors(this)

                defaultConfig {
                    targetSdk = ProjectConfig.targetSdk
                    testInstrumentationRunner = "com.infinitepower.newquiz.core.testing.NewQuizTestRunner"
                }
            }

            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(target)
            }

            dependencies {
                // Test libraries
                testImplementation(kotlin("test"))
                testImplementation(libs.findLibrary("google.truth").get())
                testImplementation(libs.findLibrary("mockk").get())
                testImplementation(libs.findLibrary("kotlinx.coroutines.test").get())
                testImplementation(libs.findLibrary("junit.jupiter.params").get())
                testImplementation(libs.findLibrary("turbine").get())

                androidTestImplementation(kotlin("test"))
                androidTestImplementation(libs.findLibrary("google.truth").get())
                androidTestImplementation(libs.findLibrary("mockk.android").get())
                androidTestImplementation(libs.findLibrary("kotlinx.coroutines.test").get())
                androidTestImplementation(libs.findLibrary("androidx.test.runner").get())
                androidTestImplementation(libs.findLibrary("androidx.test.rules").get())
                androidTestImplementation(libs.findLibrary("androidx.compose.ui.test").get())
                androidTestImplementation(project(":core:testing"))
            }

            tasks.withType<Test> {
                useJUnitPlatform()
            }

            tasks.register("testAllUnitTest") {
                // Only run debug tests
                dependsOn(
                    getTasksByName("testNormalDebugUnitTest", true),
                    getTasksByName("testFossDebugUnitTest", true),
                )
            }
        }
    }
}