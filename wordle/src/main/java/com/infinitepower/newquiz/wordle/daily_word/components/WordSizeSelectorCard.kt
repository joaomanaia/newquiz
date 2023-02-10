package com.infinitepower.newquiz.wordle.daily_word.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing

@Composable
@ExperimentalMaterial3Api
internal fun WordSizeSelectorCard(
    modifier: Modifier = Modifier,
    buttonSelectedColor: Color = MaterialTheme.colorScheme.primary,
    sizes: List<Int>,
    indexSizeSelected: Int,
    onItemClick: (index: Int) -> Unit
) {
    val spaceSmall = MaterialTheme.spacing.small
    val mediumShape = MaterialTheme.shapes.medium

    Surface(
        modifier = modifier,
        shape = mediumShape,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spaceSmall)
        ) {
            sizes.forEachIndexed { index, size ->
                WordSizeItemCard(
                    modifier = Modifier
                        .weight(1f)
                        .padding(spaceSmall),
                    buttonSelectedColor = buttonSelectedColor,
                    shape = mediumShape,
                    text = size.toString(),
                    selected = indexSizeSelected == index,
                    onClick = { onItemClick(index) }
                )
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun WordSizeItemCard(
    modifier: Modifier = Modifier,
    buttonSelectedColor: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = MaterialTheme.shapes.medium,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            buttonSelectedColor
        } else {
            MaterialTheme.colorScheme.surface
        }
    )

    Surface(
        modifier = modifier,
        onClick = onClick,
        color = backgroundColor,
        shape = shape
    ) {
        Box(
            modifier = Modifier.height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text)
        }
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun WordSizeSelectorCardPreview() {
    NewQuizTheme {
        Surface {
            WordSizeSelectorCard(
                modifier = Modifier.padding(MaterialTheme.spacing.medium),
                sizes = listOf(4, 5, 6),
                indexSizeSelected = 0,
                onItemClick = {}
            )
        }
    }
}