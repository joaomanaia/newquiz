import com.infinitepower.newquiz.configureKotlinJvm
import com.infinitepower.newquiz.libs
import com.infinitepower.newquiz.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm") // kotlin("jvm")
            }

            configureKotlinJvm()

            dependencies {
                // Test libraries
                testImplementation(kotlin("test"))
                testImplementation(libs.findLibrary("google.truth").get())
                testImplementation(libs.findLibrary("mockk").get())
                testImplementation(libs.findLibrary("kotlinx.coroutines.test").get())
                testImplementation(libs.findLibrary("junit.jupiter.params").get())
                testImplementation(libs.findLibrary("turbine").get())
            }

            tasks.withType<Test> {
                useJUnitPlatform()
            }


            tasks.register<Test>("testAllUnitTest") {
                dependsOn(
                    getTasksByName("test", true),
                )
            }
        }
    }
}