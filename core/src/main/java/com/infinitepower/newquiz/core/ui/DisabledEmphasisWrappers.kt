package com.infinitepower.newquiz.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

const val DISABLED_ALPHA = 0.38f

/**
 * Wraps the content with [CompositionLocalProvider] that overrides [LocalContentColor] with
 * an alpha of [DISABLED_ALPHA] if [enabled] is false. Otherwise, the content is not wrapped and is displayed normally.
 */
@Composable
fun DisabledLocalContentEmphasis(
    enabled: Boolean,
    content: @Composable () -> Unit
) {
    if (enabled) {
        content()
    } else {
        CompositionLocalProvider(
            LocalContentColor provides LocalContentColor.current.copy(alpha = DISABLED_ALPHA),
            content = content
        )
    }
}

@Composable
fun DisabledContentEmphasis(
    enabled: Boolean,
    content: @Composable () -> Unit
) {
    if (enabled) {
        content()
    } else {
        Box(modifier = Modifier.alpha(DISABLED_ALPHA)) {
            content()
        }
    }
}
