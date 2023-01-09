package com.infinitepower.newquiz.wordle.components

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing

/**
 * A keyboard composed of keys that can be clicked.
 *
 * @param modifier the modifier to apply to the keyboard
 * @param keys the keys to include on the keyboard
 * @param disabledKeys a set of keys that should be disabled and not clickable
 * @param onKeyClick a callback to be invoked when a key is clicked
 */
@Composable
internal fun WordleKeyBoard(
    modifier: Modifier = Modifier,
    keys: CharArray,
    disabledKeys: Set<Char>,
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
            WordleKeyboardKey(
                key = key,
                disabled = key in disabledKeys,
                onKeyClick = { onKeyClick(key) }
            )
        }
    }
}

/**
 * A single key on a keyboard.
 *
 * @param modifier the modifier to apply to the key
 * @param key the character displayed on the key
 * @param disabled whether the key should be disabled and not clickable
 * @param onKeyClick a callback to be invoked when the key is clicked
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun WordleKeyboardKey(
    modifier: Modifier = Modifier,
    key: Char,
    disabled: Boolean,
    onKeyClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .size(35.dp)
            .testTag(WordleKeyBoardTestingTags.KEY),
        onClick = onKeyClick,
        enabled = !disabled
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

@VisibleForTesting
internal object WordleKeyBoardTestingTags {
    const val KEY = "WordleKeyBoardKey"
}

@Composable
@PreviewNightLight
private fun WordKeyBoardPreview() {
    NewQuizTheme {
        Surface {
            WordleKeyBoard(
                keys = "QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray(),
                disabledKeys = "AKDTVMGUI".toSet(),
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
                disabledKeys = "136".toSet(),
                onKeyClick = {}
            )
        }
    }
}