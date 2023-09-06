import com.infinitepower.newquiz.androidTestImplementation
import com.infinitepower.newquiz.implementation
import com.infinitepower.newquiz.ksp
import com.infinitepower.newquiz.kspAndroidTest
import com.infinitepower.newquiz.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply("com.google.devtools.ksp")
            }

            dependencies {
                // Base dependencies of hilt
                implementation(libs.findLibrary("hilt.android").get())
                ksp(libs.findLibrary("hilt.compiler").get())

                // Dependencies for testing
                kspAndroidTest(libs.findLibrary("hilt.compiler").get())
                androidTestImplementation(libs.findLibrary("hilt.android.testing").get())
            }
        }
    }
}