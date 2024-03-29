package com.infinitepower.newquiz.comparison_quiz.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode

@Composable
internal fun ComparisonModeComponents(
    modifier: Modifier = Modifier,
    selectedMode: ComparisonMode = ComparisonMode.GREATER,
    onModeClick: (mode: ComparisonMode) -> Unit = {}
) {
    val spaceMedium = MaterialTheme.spacing.medium

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceMedium)
    ) {
        ComparisonModeComponent(
            mode = ComparisonMode.GREATER,
            modifier = Modifier.weight(1f),
            selected = selectedMode == ComparisonMode.GREATER,
            onClick = { onModeClick(ComparisonMode.GREATER) }
        )
        ComparisonModeComponent(
            mode = ComparisonMode.LESSER,
            modifier = Modifier.weight(1f),
            selected = selectedMode == ComparisonMode.LESSER,
            onClick = { onModeClick(ComparisonMode.LESSER) }
        )
    }
}

@Composable
@PreviewLightDark
private fun ComparisonModeComponentsPreview() {
    NewQuizTheme {
        Surface {
            ComparisonModeComponents(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
