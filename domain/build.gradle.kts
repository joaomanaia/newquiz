import com.infinitepower.newquiz.Modules

plugins {
    id("newquiz.jvm.library")
}

dependencies {
    implementation(project(Modules.model))

    implementation(libs.javax.inject)
    implementation(libs.androidx.annotation)
    implementation(libs.kotlinx.coroutines.core)
}
