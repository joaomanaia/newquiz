package com.infinitepower.newquiz.feature.maze.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.infinitepower.newquiz.core.common.compose.preview.BooleanPreviewParameterProvider
import com.infinitepower.newquiz.core.theme.NewQuizTheme

@Composable
internal fun ScrollToCurrentQuestionButton(
    modifier: Modifier = Modifier,
    state: ScrollButtonState = ScrollButtonState.SCROLL_TO_BOTTOM,
    onClick: () -> Unit = {}
) {
    if (state != ScrollButtonState.HIDDEN) {
        FloatingActionButton(
            modifier = modifier,
            onClick = onClick
        ) {
            val icon = if (state == ScrollButtonState.SCROLL_TO_TOP) {
                Icons.Rounded.ArrowUpward
            } else {
                Icons.Rounded.ArrowDownward
            }

            Icon(
                imageVector = icon,
                contentDescription = "Scroll to current question"
            )
        }
    }
}

enum class ScrollButtonState {
    HIDDEN,
    SCROLL_TO_TOP,
    SCROLL_TO_BOTTOM
}

@Composable
@PreviewLightDark
private fun ScrollToCurrentQuestionButtonPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) scrollToTop: Boolean
) {
    NewQuizTheme {
        ScrollToCurrentQuestionButton(
            state = if (scrollToTop) ScrollButtonState.SCROLL_TO_TOP else ScrollButtonState.SCROLL_TO_BOTTOM
        )
    }
}
