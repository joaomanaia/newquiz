package com.infinitepower.newquiz.wordle.components

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.CustomColor
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.extendedColors
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.wordle.WordleChar
import com.infinitepower.newquiz.model.wordle.WordleItem
import com.infinitepower.newquiz.model.wordle.WordleRowItem

@Composable
internal fun WordleRowComponent(
    modifier: Modifier = Modifier,
    wordleRowItem: WordleRowItem,
    onItemClick: (index: Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        modifier = modifier
    ) {
        wordleRowItem.items.forEachIndexed { index, item ->
            WordleComponent(
                item = item,
                onClick = { onItemClick(index) }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun WordleComponent(
    modifier: Modifier = Modifier,
    item: WordleItem,
    onClick: () -> Unit
) {
    val backgroundColor = when (item) {
        is WordleItem.Correct -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Green)
        is WordleItem.Present -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Yellow)
        else -> MaterialTheme.colorScheme.surface
    }
    val backgroundColorAnimated by animateColorAsState(targetValue = backgroundColor)

    val textColor = when (item) {
        is WordleItem.Correct -> MaterialTheme.extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Green)
        is WordleItem.Present -> MaterialTheme.extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Yellow)
        else -> MaterialTheme.colorScheme.onSurface
    }
    val textColorAnimated by animateColorAsState(targetValue = textColor)

    val elevation = if (item.char.isEmpty()) 0.dp else 8.dp
    val elevationAnimated by animateDpAsState(targetValue = elevation)

    val stroke = if (item.char.isEmpty()) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    } else null

    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .size(50.dp)
            .testTag(WordleRowComponentTestingTags.WORDLE_COMPONENT_SURFACE)
            .semantics {
                contentDescription = when (item) {
                    is WordleItem.Empty -> "Item empty"
                    is WordleItem.None -> "Item ${item.char} none"
                    is WordleItem.Present -> "Item ${item.char} present"
                    is WordleItem.Correct -> "Item ${item.char} correct"
                }
            },
        color = backgroundColorAnimated,
        tonalElevation = elevationAnimated,
        border = stroke,
        onClick = onClick,
        enabled = item is WordleItem.None
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.char.toString(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = textColorAnimated
            )
        }
    }
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal object WordleRowComponentTestingTags {
    const val WORDLE_COMPONENT_SURFACE = "WORDLE_COMPONENT_SURFACE"
}

@Composable
@PreviewNightLight
private fun WordleComponentPreview() {
    val item = WordleItem.fromChar('A')

    NewQuizTheme {
        Surface {
            WordleComponent(
                modifier = Modifier.padding(16.dp),
                item = item,
                onClick = {}
            )
            WordleComponent(
                modifier = Modifier.padding(16.dp),
                item = item,
                onClick = {}
            )
        }
    }
}

@Composable
@PreviewNightLight
private fun WordleRowComponentPreview() {
    val item = WordleRowItem(
        items = listOf(
            WordleItem.Empty,
            WordleItem.None(char = WordleChar('A')),
            WordleItem.Present(char = WordleChar('B')),
            WordleItem.Correct(char = WordleChar('C')),
        )
    )

    NewQuizTheme {
        Surface {
            WordleRowComponent(
                modifier = Modifier.padding(16.dp),
                wordleRowItem = item,
                onItemClick = {}
            )
        }
    }
}