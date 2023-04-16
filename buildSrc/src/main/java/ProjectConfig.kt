import org.gradle.api.JavaVersion

object ProjectConfig {
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    const val applicationId = "com.infinitepower.newquiz"

    const val compileSdk = 33

    const val minSdk = 21

    const val targetSdk = 33

    const val versionCode = 13

    const val versionName = "1.5.3"

    val javaVersionCompatibility = JavaVersion.VERSION_17

    const val jvmTargetVersion = "17"
}
