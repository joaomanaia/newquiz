import com.android.build.api.dsl.LibraryExtension
import com.infinitepower.newquiz.androidTestImplementation
import com.infinitepower.newquiz.configureKotlinAndroid
import com.infinitepower.newquiz.libs
import com.infinitepower.newquiz.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

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

            configurations.configureEach {
                resolutionStrategy {
                    force(libs.findLibrary("junit4").get())
                }
            }

            dependencies {
                testImplementation(kotlin("test"))
                androidTestImplementation(kotlin("test"))
            }
        }
    }
}