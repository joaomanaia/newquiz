package com.infinitepower.newquiz.wordle.components

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.wordle.WordleQuizType

/**
 * A keyboard composed of keys that can be clicked.
 *
 * @param modifier the modifier to apply to the keyboard
 * @param keys the keys to include on the keyboard
 * @param disabledKeys a set of keys that should be disabled and not clickable
 * @param onKeyClick a callback to be invoked when a key is clicked
 */
@Composable
@ExperimentalLayoutApi
internal fun WordleKeyBoard(
    modifier: Modifier = Modifier,
    rowLayout: Boolean = false,
    windowWidthSizeClass: WindowWidthSizeClass,
    wordleQuizType: WordleQuizType,
    keys: CharArray,
    disabledKeys: Set<Char>,
    contentPadding: PaddingValues = PaddingValues(),
    onKeyClick: (key: Char) -> Unit
) {
    val spaceSmall = MaterialTheme.spacing.small

    val maxWidth = when {
        windowWidthSizeClass == WindowWidthSizeClass.Medium && !rowLayout -> 0.5f
        windowWidthSizeClass == WindowWidthSizeClass.Expanded && !rowLayout -> 0.35f
        else -> 1f
    }

    if (wordleQuizType == WordleQuizType.TEXT) {
        FlowRow(
            modifier = modifier
                .padding(contentPadding)
                .fillMaxWidth(maxWidth),
            verticalArrangement = Arrangement.spacedBy(spaceSmall),
            horizontalArrangement = Arrangement.spacedBy(spaceSmall, Alignment.CenterHorizontally)
        ) {
            keys.forEach { key ->
                WordleKeyboardKey(
                    key = key,
                    disabled = key in disabledKeys,
                    onKeyClick = { onKeyClick(key) }
                )
            }
        }
    } else {
        val chuckSize = if (wordleQuizType == WordleQuizType.NUMBER) 3 else 4

        val keyList = keys.toList()

        LazyVerticalGrid(
            columns = GridCells.Fixed(chuckSize),
            modifier = modifier.fillMaxWidth(maxWidth),
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.spacedBy(spaceSmall, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(spaceSmall, Alignment.CenterVertically),
            contentPadding = contentPadding
        ) {
            items(items = keyList) { key ->
                WordleKeyboardKey(
                    key = key,
                    disabled = key in disabledKeys,
                    onKeyClick = { onKeyClick(key) },
                    modifier = Modifier.size(35.dp)
                )
            }
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
@PreviewLightDark
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalLayoutApi::class)
private fun WordKeyBoardPreview() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val windowWidthClass = WindowSizeClass.calculateFromSize(DpSize(screenWidth, screenHeight))

    NewQuizTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                WordleKeyBoard(
                    keys = "QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray(),
                    disabledKeys = "AKDTVMGUI".toSet(),
                    onKeyClick = {},
                    wordleQuizType = WordleQuizType.TEXT,
                    windowWidthSizeClass = windowWidthClass.widthSizeClass
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalLayoutApi::class)
private fun WordKeyBoardNumbersPreview() {
    val allNumbers = "1234567890"

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val windowWidthClass = WindowSizeClass.calculateFromSize(DpSize(screenWidth, screenHeight))

    NewQuizTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                WordleKeyBoard(
                    keys = allNumbers.toList().toCharArray(),
                    disabledKeys = "136".toSet(),
                    onKeyClick = {},
                    wordleQuizType = WordleQuizType.NUMBER,
                    windowWidthSizeClass = windowWidthClass.widthSizeClass
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalLayoutApi::class)
private fun WordKeyBoardMathFormulaPreview() {
    val allNumbers = "1234567890+-*/="

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val windowWidthClass = WindowSizeClass.calculateFromSize(DpSize(screenWidth, screenHeight))

    NewQuizTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                WordleKeyBoard(
                    keys = allNumbers.toList().toCharArray(),
                    disabledKeys = "136".toSet(),
                    onKeyClick = {},
                    wordleQuizType = WordleQuizType.MATH_FORMULA,
                    windowWidthSizeClass = windowWidthClass.widthSizeClass
                )
            }
        }
    }
}