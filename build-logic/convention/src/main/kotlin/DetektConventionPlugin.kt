import com.infinitepower.newquiz.libs
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("io.gitlab.arturbosch.detekt")

                withPlugin("io.gitlab.arturbosch.detekt") {
                    val rootProject = target.rootProject

                    target.extensions.configure<DetektExtension> {
                        buildUponDefaultConfig = true
                        baseline = target.file("detekt-baseline.xml")
                        basePath = rootProject.projectDir.absolutePath

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
                    }

                    val detektTask = target.tasks.named("detekt", Detekt::class.java)
                    detektTask.configure {
                        reports.sarif.required.set(true)
                    }
                }
            }

            dependencies {
                add(
                    "detektPlugins",
                    libs.findLibrary("detekt.compose").get()
                )
            }
        }
    }
}
