package com.infinitepower.newquiz

import org.gradle.api.artifacts.dsl.DependencyHandler

internal fun DependencyHandler.implementation(dependencyNotation: Any) =
    add("implementation", dependencyNotation)

internal fun DependencyHandler.testImplementation(dependencyNotation: Any) =
    add("testImplementation", dependencyNotation)

internal fun DependencyHandler.androidTestImplementation(dependencyNotation: Any) =
    add("androidTestImplementation", dependencyNotation)

internal fun DependencyHandler.ksp(dependencyNotation: Any) =
    add("ksp", dependencyNotation)

internal fun DependencyHandler.kspAndroidTest(dependencyNotation: Any) =
    add("kspAndroidTest", dependencyNotation)

internal fun DependencyHandler.normalImplementation(dependencyNotation: Any) =
    add("normalImplementation", dependencyNotation)

internal fun DependencyHandler.coreLibraryDesugaring(dependencyNotation: Any) =
    add("coreLibraryDesugaring", dependencyNotation)

internal fun DependencyHandler.debugImplementation(dependencyNotation: Any) =
    add("debugImplementation", dependencyNotation)
