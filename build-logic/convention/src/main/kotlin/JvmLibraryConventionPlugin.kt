import com.infinitepower.newquiz.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm") // kotlin("jvm")
            }

            configureKotlinJvm()

            tasks.withType<Test> {
                useJUnitPlatform()
            }
        }
    }
}