import com.infinitepower.newquiz.debugImplementation
import com.infinitepower.newquiz.implementation
import com.infinitepower.newquiz.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("newquiz.android.library.compose")
                apply("newquiz.android.hilt")
            }

            dependencies {
                implementation(project(":model"))
                implementation(project(":core"))
                implementation(project(":core:analytics"))

                implementation(libs.findLibrary("androidx.compose.foundation").get())
                implementation(libs.findLibrary("androidx.compose.material3").get())
                implementation(libs.findLibrary("androidx.compose.runtime").get())
                implementation(libs.findLibrary("androidx.compose.ui.tooling.preview").get())

                debugImplementation(libs.findLibrary("androidx.compose.ui.tooling").get())

                implementation(libs.findLibrary("hilt.navigationCompose").get())
                implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                implementation(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())

                implementation(libs.findLibrary("kotlinx.coroutines.android").get())
            }
        }
    }
}
