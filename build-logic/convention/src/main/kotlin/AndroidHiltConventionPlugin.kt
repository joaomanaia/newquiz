import com.infinitepower.newquiz.implementation
import com.infinitepower.newquiz.kapt
import com.infinitepower.newquiz.kaptAndroidTest
import com.infinitepower.newquiz.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply("org.jetbrains.kotlin.kapt")
            }

            dependencies {
                implementation(libs.findLibrary("hilt.android").get())
                kapt(libs.findLibrary("hilt.compiler").get())
                kaptAndroidTest(libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}