import com.android.build.api.dsl.ApplicationExtension
import com.infinitepower.newquiz.NewQuizFlavor
import com.infinitepower.newquiz.ProjectConfig
import com.infinitepower.newquiz.configureFlavors
import com.infinitepower.newquiz.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                configureFlavors(this)

                defaultConfig {
                    targetSdk = ProjectConfig.targetSdk
                }
            }

            // Apply the Firebase plugin only for the "Normal" build type
            val gradleTaskRequests = gradle.startParameter.taskRequests.toString()
            val normalFlavor = NewQuizFlavor.normal.name.uppercaseFirstChar()
            val fossFlavor = NewQuizFlavor.foss.name.uppercaseFirstChar()

            // Check if the flavor name is in the gradle task requests,
            // like "assembleNormalDebug" or "assembleFossDebug"
            if (gradleTaskRequests.contains(normalFlavor)) {
                apply(plugin = "newquiz.android.application.firebase")
                logger.info("Applied Firebase plugin for normal build type")
            } else if (gradleTaskRequests.contains(fossFlavor)) {
                // TODO: Remove this once the firebase is completely removed from foss builds
                apply(plugin = "com.google.gms.google-services")
                logger.warn("Applied Google Services plugin for foss build type, it will be removed once the firebase is completely removed from foss builds")
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