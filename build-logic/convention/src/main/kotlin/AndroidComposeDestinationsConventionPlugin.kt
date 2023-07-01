import com.google.devtools.ksp.gradle.KspExtension
import com.infinitepower.newquiz.implementation
import com.infinitepower.newquiz.ksp
import com.infinitepower.newquiz.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeDestinationsConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("com.google.devtools.ksp")

            dependencies {
                implementation(libs.findLibrary("compose.destinations.core").get())
                ksp(libs.findLibrary("compose.destinations.ksp").get())
            }

            extensions.configure<KspExtension> {
                val mode = "destinations"
                val moduleName = target.name

                logger.info("Configuring compose-destinations for $moduleName, with mode: $mode")

                arg("compose-destinations.mode", mode)
                arg("compose-destinations.moduleName", moduleName)
            }
        }
    }
}