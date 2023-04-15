package com.infinitepower.newquiz.comparison_quiz.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonModeByFirst

@Composable
internal fun ComparisonModeComponents(
    modifier: Modifier = Modifier,
    selectedMode: ComparisonModeByFirst = ComparisonModeByFirst.GREATER,
    onModeClick: (mode: ComparisonModeByFirst) -> Unit = {}
) {
    val spaceMedium = MaterialTheme.spacing.medium

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceMedium)
    ) {
        ComparisonModeComponent(
            mode = ComparisonModeByFirst.GREATER,
            modifier = Modifier.weight(1f),
            selected = selectedMode == ComparisonModeByFirst.GREATER,
            onClick = { onModeClick(ComparisonModeByFirst.GREATER) }
        )
        ComparisonModeComponent(
            mode = ComparisonModeByFirst.LESSER,
            modifier = Modifier.weight(1f),
            selected = selectedMode == ComparisonModeByFirst.LESSER,
            onClick = { onModeClick(ComparisonModeByFirst.LESSER) }
        )
    }
}

@Composable
@PreviewNightLight
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
