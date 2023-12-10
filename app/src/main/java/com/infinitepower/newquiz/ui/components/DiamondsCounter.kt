package com.infinitepower.newquiz.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing

@Composable
internal fun DiamondsCounter(
    modifier: Modifier = Modifier,
    diamonds: UInt
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        Text(
            text = "$diamonds \uD83D\uDC8E",
            modifier = Modifier.padding(
                horizontal = MaterialTheme.spacing.small,
                vertical = MaterialTheme.spacing.extraSmall
            ),
        )
    }
}

@Composable
@PreviewNightLight
private fun DiamondsCounterPreview() {
    NewQuizTheme {
        Surface {
            DiamondsCounter(
                modifier = Modifier.padding(16.dp),
                diamonds = 100u
            )
        }
    }
}