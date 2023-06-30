import com.android.build.api.dsl.LibraryExtension
import com.infinitepower.newquiz.androidTestImplementation
import com.infinitepower.newquiz.configureKotlinAndroid
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
            }

            dependencies {
                // Test libraries
                testImplementation(kotlin("test"))
                testImplementation(libs.findLibrary("google.truth").get())
                testImplementation(libs.findLibrary("mockk").get())
                testImplementation(libs.findLibrary("kotlinx.coroutines.test").get())

                androidTestImplementation(kotlin("test"))
                androidTestImplementation(libs.findLibrary("google.truth").get())
                androidTestImplementation(libs.findLibrary("mockk").get())
                androidTestImplementation(libs.findLibrary("kotlinx.coroutines.test").get())
            }

            tasks.withType<Test> {
                useJUnitPlatform()
            }
        }
    }
}