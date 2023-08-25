import com.infinitepower.newquiz.implementation
import com.infinitepower.newquiz.libs
import com.infinitepower.newquiz.normalImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.firebase-perf")
                apply("com.google.firebase.crashlytics")
            }

            dependencies {
                val bom = libs.findLibrary("firebase-bom").get()

                implementation(platform(bom))
                normalImplementation(libs.findLibrary("firebase-analytics-ktx").get())
                normalImplementation(libs.findLibrary("firebase-crashlytics-ktx").get())
                normalImplementation(libs.findLibrary("firebase-perf-ktx").get())
            }
        }
    }
}