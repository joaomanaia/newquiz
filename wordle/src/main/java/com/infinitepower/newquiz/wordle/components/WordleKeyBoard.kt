package com.infinitepower.newquiz.wordle.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing

@Composable
internal fun WordleKeyBoard(
    modifier: Modifier = Modifier,
    keys: CharArray,
    keysDisabled: List<Char>,
    onKeyClick: (key: Char) -> Unit
) {
    val spaceSmall = MaterialTheme.spacing.small

    FlowRow(
        modifier = modifier,
        mainAxisSpacing = spaceSmall,
        crossAxisSpacing = spaceSmall,
        mainAxisAlignment = FlowMainAxisAlignment.Center
    ) {
        keys.forEach { key ->
            WordleKeyBoardItem(
                key = key,
                enabled = key !in keysDisabled,
                onClick = { onKeyClick(key) }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun WordleKeyBoardItem(
    modifier: Modifier = Modifier,
    key: Char,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.size(35.dp),
        onClick = onClick,
        enabled = enabled
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = key.toString(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@PreviewNightLight
private fun WordKeyBoardPreview() {
    NewQuizTheme {
        Surface {
            WordleKeyBoard(
                keys = "QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray(),
                keysDisabled = "AKDTVMGUI".toList(),
                onKeyClick = {}
            )
        }
    }
}

@Composable
@PreviewNightLight
private fun WordKeyBoardNumbersPreview() {
    val allNumbers = '0'..'9'

    NewQuizTheme {
        Surface {
            WordleKeyBoard(
                keys = allNumbers.toList().toCharArray(),
                keysDisabled = "136".toList(),
                onKeyClick = {}
            )
        }
    }
}