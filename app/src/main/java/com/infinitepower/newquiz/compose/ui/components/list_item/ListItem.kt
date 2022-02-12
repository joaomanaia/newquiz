package com.infinitepower.newquiz.compose.ui.components.list_item

import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.compose.core.util.list.fastForEachIndexed
import kotlin.math.max

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    secondaryText: @Composable (() -> Unit)? = null,
    singleLineSecondaryText: Boolean = true,
    overlineText: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    text: @Composable () -> Unit
) {
    val typography = MaterialTheme.typography

    val styledText = applyTextStyle(typography.titleMedium, ContentAlpha.high, text)!!
    val styledSecondaryText = applyTextStyle(typography.bodyMedium, ContentAlpha.medium, secondaryText)
    val styledOverlineText = applyTextStyle(typography.labelSmall, ContentAlpha.high, overlineText)
    val styledTrailing = applyTextStyle(typography.bodySmall, ContentAlpha.high, trailing)

    val semanticsModifier = modifier.semantics(mergeDescendants = true) {}

    if (styledSecondaryText == null && styledOverlineText == null) {
        OneLine.ListItem(semanticsModifier, icon, styledText, styledTrailing)
    } else if (
        (styledOverlineText == null && singleLineSecondaryText) || styledSecondaryText == null
    ) {
        TwoLine.ListItem(
            semanticsModifier,
            icon,
            styledText,
            styledSecondaryText,
            styledOverlineText,
            styledTrailing
        )
    } else {
        ThreeLine.ListItem(
            semanticsModifier,
            icon,
            styledText,
            styledSecondaryText,
            styledOverlineText,
            styledTrailing
        )
    }
}

private object OneLine {
    // TODO(popam): support wide icons
    // TODO(popam): convert these to sp
    // List item related defaults.
    private val MinHeight = 48.dp
    private val MinHeightWithIcon = 56.dp

    // Icon related defaults.
    private val IconMinPaddedWidth = 40.dp
    private val IconLeftPadding = 16.dp
    private val IconVerticalPadding = 8.dp

    // Content related defaults.
    private val ContentLeftPadding = 16.dp
    private val ContentRightPadding = 16.dp

    // Trailing related defaults.
    private val TrailingRightPadding = 16.dp

    @Composable
    fun ListItem(
        modifier: Modifier = Modifier,
        icon: @Composable (() -> Unit)?,
        text: @Composable (() -> Unit),
        trailing: @Composable (() -> Unit)?
    ) {
        val minHeight = if (icon == null) MinHeight else MinHeightWithIcon
        Row(modifier.heightIn(min = minHeight)) {
            if (icon != null) {
                Box(
                    Modifier.align(Alignment.CenterVertically)
                        .widthIn(min = IconLeftPadding + IconMinPaddedWidth)
                        .padding(
                            start = IconLeftPadding,
                            top = IconVerticalPadding,
                            bottom = IconVerticalPadding
                        ),
                    contentAlignment = Alignment.CenterStart
                ) { icon() }
            }
            Box(
                Modifier.weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(start = ContentLeftPadding, end = ContentRightPadding),
                contentAlignment = Alignment.CenterStart
            ) { text() }
            if (trailing != null) {
                Box(
                    Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = TrailingRightPadding)
                ) { trailing() }
            }
        }
    }
}

private object TwoLine {
    // List item related defaults.
    private val MinHeight = 64.dp
    private val MinHeightWithIcon = 72.dp

    // Icon related defaults.
    private val IconMinPaddedWidth = 40.dp
    private val IconLeftPadding = 16.dp
    private val IconVerticalPadding = 16.dp

    // Content related defaults.
    private val ContentLeftPadding = 16.dp
    private val ContentRightPadding = 16.dp
    private val OverlineBaselineOffset = 24.dp
    private val OverlineToPrimaryBaselineOffset = 20.dp
    private val PrimaryBaselineOffsetNoIcon = 28.dp
    private val PrimaryBaselineOffsetWithIcon = 32.dp
    private val PrimaryToSecondaryBaselineOffsetNoIcon = 20.dp
    private val PrimaryToSecondaryBaselineOffsetWithIcon = 20.dp

    // Trailing related defaults.
    private val TrailingRightPadding = 16.dp

    @Composable
    fun ListItem(
        modifier: Modifier = Modifier,
        icon: @Composable (() -> Unit)?,
        text: @Composable (() -> Unit),
        secondaryText: @Composable (() -> Unit)?,
        overlineText: @Composable (() -> Unit)?,
        trailing: @Composable (() -> Unit)?
    ) {
        val minHeight = if (icon == null) MinHeight else MinHeightWithIcon
        Row(modifier.heightIn(min = minHeight)) {
            val columnModifier = Modifier.weight(1f)
                .padding(start = ContentLeftPadding, end = ContentRightPadding)

            if (icon != null) {
                Box(
                    Modifier
                        .sizeIn(
                            minWidth = IconLeftPadding + IconMinPaddedWidth,
                            minHeight = minHeight
                        )
                        .padding(
                            start = IconLeftPadding,
                            top = IconVerticalPadding,
                            bottom = IconVerticalPadding
                        ),
                    contentAlignment = Alignment.TopStart
                ) { icon() }
            }

            if (overlineText != null) {
                BaselinesOffsetColumn(
                    listOf(OverlineBaselineOffset, OverlineToPrimaryBaselineOffset),
                    columnModifier
                ) {
                    overlineText()
                    text()
                }
            } else {
                BaselinesOffsetColumn(
                    listOf(
                        if (icon != null) {
                            PrimaryBaselineOffsetWithIcon
                        } else {
                            PrimaryBaselineOffsetNoIcon
                        },
                        if (icon != null) {
                            PrimaryToSecondaryBaselineOffsetWithIcon
                        } else {
                            PrimaryToSecondaryBaselineOffsetNoIcon
                        }
                    ),
                    columnModifier
                ) {
                    text()
                    secondaryText!!()
                }
            }
            if (trailing != null) {
                OffsetToBaselineOrCenter(
                    if (icon != null) {
                        PrimaryBaselineOffsetWithIcon
                    } else {
                        PrimaryBaselineOffsetNoIcon
                    }
                ) {
                    Box(
                        // TODO(popam): find way to center and wrap content without minHeight
                        Modifier.heightIn(min = minHeight)
                            .padding(end = TrailingRightPadding),
                        contentAlignment = Alignment.Center
                    ) { trailing() }
                }
            }
        }
    }
}

private object ThreeLine {
    // List item related defaults.
    private val MinHeight = 88.dp

    // Icon related defaults.
    private val IconMinPaddedWidth = 40.dp
    private val IconLeftPadding = 16.dp
    private val IconThreeLineVerticalPadding = 16.dp

    // Content related defaults.
    private val ContentLeftPadding = 16.dp
    private val ContentRightPadding = 16.dp
    private val ThreeLineBaselineFirstOffset = 28.dp
    private val ThreeLineBaselineSecondOffset = 20.dp
    private val ThreeLineBaselineThirdOffset = 20.dp
    private val ThreeLineTrailingTopPadding = 16.dp

    // Trailing related defaults.
    private val TrailingRightPadding = 16.dp

    @Composable
    fun ListItem(
        modifier: Modifier = Modifier,
        icon: @Composable (() -> Unit)?,
        text: @Composable (() -> Unit),
        secondaryText: @Composable (() -> Unit),
        overlineText: @Composable (() -> Unit)?,
        trailing: @Composable (() -> Unit)?
    ) {
        Row(modifier.heightIn(min = MinHeight)) {
            if (icon != null) {
                val minSize = IconLeftPadding + IconMinPaddedWidth
                Box(
                    Modifier
                        .sizeIn(minWidth = minSize, minHeight = minSize)
                        .padding(
                            start = IconLeftPadding,
                            top = IconThreeLineVerticalPadding,
                            bottom = IconThreeLineVerticalPadding
                        ),
                    contentAlignment = Alignment.CenterStart
                ) { icon() }
            }
            BaselinesOffsetColumn(
                listOf(
                    ThreeLineBaselineFirstOffset,
                    ThreeLineBaselineSecondOffset,
                    ThreeLineBaselineThirdOffset
                ),
                Modifier.weight(1f)
                    .padding(start = ContentLeftPadding, end = ContentRightPadding)
            ) {
                if (overlineText != null) overlineText()
                text()
                secondaryText()
            }
            if (trailing != null) {
                OffsetToBaselineOrCenter(
                    ThreeLineBaselineFirstOffset - ThreeLineTrailingTopPadding,
                    Modifier.padding(top = ThreeLineTrailingTopPadding, end = TrailingRightPadding),
                    trailing
                )
            }
        }
    }
}

@Composable
private fun BaselinesOffsetColumn(
    offsets: List<Dp>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(content, modifier) { measurables, constraints ->
        val childConstraints = constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity)
        val placeables = measurables.map { it.measure(childConstraints) }

        val containerWidth = placeables.fold(0) { maxWidth, placeable ->
            max(maxWidth, placeable.width)
        }
        val y = Array(placeables.size) { 0 }
        var containerHeight = 0
        placeables.fastForEachIndexed { index, placeable ->
            val toPreviousBaseline = if (index > 0) {
                placeables[index - 1].height - placeables[index - 1][LastBaseline]
            } else 0
            val topPadding = max(
                0,
                offsets[index].roundToPx() - placeable[FirstBaseline] - toPreviousBaseline
            )
            y[index] = topPadding + containerHeight
            containerHeight += topPadding + placeable.height
        }

        layout(containerWidth, containerHeight) {
            placeables.fastForEachIndexed { index, placeable ->
                placeable.placeRelative(0, y[index])
            }
        }
    }
}

@Composable
private fun OffsetToBaselineOrCenter(
    offset: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(content, modifier) { measurables, constraints ->
        val placeable = measurables[0].measure(constraints.copy(minHeight = 0))
        val baseline = placeable[FirstBaseline]
        val y: Int
        val containerHeight: Int
        if (baseline != AlignmentLine.Unspecified) {
            y = offset.roundToPx() - baseline
            containerHeight = max(constraints.minHeight, y + placeable.height)
        } else {
            containerHeight = max(constraints.minHeight, placeable.height)
            y = Alignment.Center.align(
                IntSize.Zero,
                IntSize(0, containerHeight - placeable.height),
                layoutDirection
            ).y
        }
        layout(placeable.width, containerHeight) {
            placeable.placeRelative(0, y)
        }
    }
}

private fun applyTextStyle(
    textStyle: TextStyle,
    contentAlpha: Float,
    icon: @Composable (() -> Unit)?
): @Composable (() -> Unit)? {
    if (icon == null) return null
    return {
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha) {
            ProvideTextStyle(textStyle, icon)
        }
    }
}
