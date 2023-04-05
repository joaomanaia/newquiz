package com.infinitepower.newquiz.core_test.compose.theme

import androidx.compose.runtime.Composable
import com.infinitepower.newquiz.core.theme.NewQuizTheme

@Composable
fun NewQuizTestTheme(
    content: @Composable () -> Unit
) {
    NewQuizTheme(
        darkTheme = false,
        dynamicColor = false,
        content = content
    )
}