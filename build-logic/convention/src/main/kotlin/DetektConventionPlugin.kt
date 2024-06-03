import com.infinitepower.newquiz.libs
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(
                libs.findLibrary("detekt.gradlePlugin").get().get().group.toString()
            )

            tasks.withType<Detekt> {
                buildUponDefaultConfig = true
                basePath = target.rootProject.projectDir.absolutePath

                val localDetektConfig = target.file("detekt.yml")
                val rootDetektConfig = target.rootProject.file("detekt.yml")
                val rootDetektComposeConfig = target.rootProject.file("detekt-compose.yml")
                if (localDetektConfig.exists()) {
                    config.from(
                        localDetektConfig,
                        rootDetektConfig,
                        rootDetektComposeConfig
                    )
                } else {
                    config.from(rootDetektConfig, rootDetektComposeConfig)
                }

                reports {
                    sarif.required.set(true)
                }
            }

            dependencies {
                add("detektPlugins", libs.findLibrary("detekt.compose").get())
            }
        }
    }
}
