import com.android.build.api.dsl.ApplicationExtension
import com.infinitepower.newquiz.ProjectConfig
import com.infinitepower.newquiz.configureFlavors
import com.infinitepower.newquiz.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

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
        }
    }
}