package com.infinitepower.newquiz.core.theme

import androidx.annotation.Keep
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

@Keep
data class AnimationsEnabled(
    val global: Boolean = true,
    val wordle: Boolean = true
)

val LocalAnimationsEnabled = compositionLocalOf { AnimationsEnabled() }

val MaterialTheme.animationsEnabled: AnimationsEnabled
    @Composable
    @ReadOnlyComposable
    get() = LocalAnimationsEnabled.current
