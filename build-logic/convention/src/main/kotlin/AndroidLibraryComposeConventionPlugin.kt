import com.android.build.api.dsl.LibraryExtension
import com.infinitepower.newquiz.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("newquiz.android.library")
            }

            extensions.configure<LibraryExtension> {
                configureAndroidCompose(this)

                defaultConfig {
                    testInstrumentationRunner = "com.infinitepower.newquiz.core_test.HiltTestRunner"
                }
            }
        }
    }
}
