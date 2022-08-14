package com.infinitepower.newquiz.core.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ContentAlpha
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@Composable
fun StatusWrapper(
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.alpha(if (enabled) ContentAlpha.high else ContentAlpha.disabled)
    ) {
        content()
    }
}