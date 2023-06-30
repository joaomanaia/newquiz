import com.infinitepower.newquiz.androidTestImplementation
import com.infinitepower.newquiz.implementation
import com.infinitepower.newquiz.kapt
import com.infinitepower.newquiz.kaptAndroidTest
import com.infinitepower.newquiz.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply("org.jetbrains.kotlin.kapt")
            }

            dependencies {
                // Base dependencies of hilt
                implementation(libs.findLibrary("hilt.android").get())
                kapt(libs.findLibrary("hilt.compiler").get())

                // Dependencies for testing
                kaptAndroidTest(libs.findLibrary("hilt.compiler").get())
                androidTestImplementation(libs.findLibrary("hilt.android.testing").get())
            }

            extensions.getByType<KaptExtension>().apply {
                correctErrorTypes = true
            }
        }
    }
}