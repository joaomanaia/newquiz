package com.infinitepower.newquiz.core.testing.ui.theme

import androidx.compose.runtime.Composable
import com.infinitepower.newquiz.core.theme.NewQuizTheme

@Composable
fun NewQuizTestTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    NewQuizTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        content = content
    )
}
