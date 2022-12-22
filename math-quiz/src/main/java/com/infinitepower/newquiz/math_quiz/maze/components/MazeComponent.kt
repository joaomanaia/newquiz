package com.infinitepower.newquiz.math_quiz.maze.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.math_quiz.MathFormula
import com.infinitepower.newquiz.model.math_quiz.maze.MathQuizMaze
import com.infinitepower.newquiz.model.math_quiz.maze.MazePoint
import com.infinitepower.newquiz.model.math_quiz.maze.generateMazePointsBottomToTop
import com.infinitepower.newquiz.model.math_quiz.maze.isInsideCircle
import com.infinitepower.newquiz.model.math_quiz.maze.toMazePoint
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlin.math.abs
import kotlin.math.pow

@Composable
internal fun MazeComponent(
    modifier: Modifier = Modifier,
    items: List<MathQuizMaze.MazeItem>,
    onItemClick: (item: MathQuizMaze.MazeItem) -> Unit
) {
    val localDensity = LocalDensity.current
    val spaceLarge = MaterialTheme.spacing.large

    BoxWithConstraints(
        modifier = modifier.height(2000.dp)
    ) {
        val startPoint = with(localDensity) {
            MazePoint(
                x = maxWidth.toPx() / 2,
                y = (maxHeight - spaceLarge).toPx()
            )
        }

        MazeComponentImpl(
            modifier = Modifier.fillMaxWidth(),
            items = items,
            startPoint = startPoint,
            onClick = { index -> onItemClick(items[index]) }
        )
    }
}

private fun getContentColor(
    items: List<MathQuizMaze.MazeItem>,
    index: Int,
    colorPrimary: Color,
    colorSurfaceVariant: Color
): Color {
    val itemPlayed = items.getOrNull(index)?.played == true
    val isPlayItem = isPlayItem(items, index)

    return if (itemPlayed || isPlayItem) colorPrimary else colorSurfaceVariant
}

private fun isPlayItem(
    items: List<MathQuizMaze.MazeItem>,
    index: Int
): Boolean {
    val itemPlayed = items.getOrNull(index)?.played == true
    val prevItem = items.getOrNull(index - 1)
    val prevItemPlayed = prevItem?.played == true

    val nextItemPlayed = items.getOrNull(index + 1)?.played == true

    val firstItemPlay = prevItem == null

    return (firstItemPlay || prevItemPlayed) && !itemPlayed && !nextItemPlayed
}

@Composable
private fun MazeComponentImpl(
    modifier: Modifier = Modifier,
    items: List<MathQuizMaze.MazeItem>,
    startPoint: MazePoint,
    onClick: (index: Int) -> Unit
) {
    val localDensity = LocalDensity.current

    val colorPrimary = MaterialTheme.colorScheme.primary
    val colorSecondary = MaterialTheme.colorScheme.secondary
    val colorSurface = MaterialTheme.colorScheme.surface
    val colorSurfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    val incrementPoint = with(localDensity) {
        MazePoint(x = 100.dp.toPx(), y = 100.dp.toPx())
    }

    val circleRadius = with(localDensity) { 30.dp.toPx() }

    val mazePoints = remember(items.size, startPoint) {
        generateMazePointsBottomToTop(
            startPoint = MazePoint(startPoint.x, startPoint.y),
            increment = incrementPoint
        ).take(items.size).toList()
    }

    val height = remember(mazePoints) {
        if (mazePoints.isEmpty()) return@remember 0.dp

        val heightPx = abs(mazePoints.first().y) + abs(mazePoints.last().y)
        heightPx.dp
    }

    var topScroll by remember { mutableStateOf(0f) }

    val topY = with(localDensity) {
        abs(mazePoints.lastOrNull()?.y ?: 0f) + 100.dp.toPx()
    }

    val playVector = Icons.Rounded.PlayArrow
    val playPainter = rememberVectorPainter(image = playVector)

    val checkVector = Icons.Rounded.Check
    val checkPainter = rememberVectorPainter(image = checkVector)

    val lockVector = Icons.Rounded.Lock
    val lockPainter = rememberVectorPainter(image = lockVector)

    Canvas(
        modifier = modifier
            .height(height)
            .zIndex(1f)
    ) {
        translate(top = topScroll) {
            mazePoints.forEachIndexed { index, point ->
                val pointOffset = Offset(point.x, point.y)

                val circleColor = getContentColor(items, index, colorPrimary, colorSurfaceVariant)

                val startOffset = mazePoints
                    .getOrNull(index - 1)
                    ?.let { prevPoint ->
                        Offset(
                            x = prevPoint.x,
                            y = prevPoint.y
                        )
                    } ?: pointOffset

                drawLine(
                    color = circleColor,
                    start = startOffset,
                    end = pointOffset,
                    strokeWidth = 12.dp.toPx()
                )
            }
        }
    }

    Canvas(
        modifier = modifier
            .height(height)
            .zIndex(2f)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { tapOffset ->
                        val tapPoint = tapOffset.toMazePoint()
                        Log.d("MazeComponent", "Clicked in position: $tapOffset")

                        mazePoints.forEachIndexed { index, mazePoint ->
                            val realMazePoint = mazePoint.copy(y = mazePoint.y + topScroll)

                            if (tapPoint.isInsideCircle(realMazePoint, circleRadius)) {
                                val isPlayItem = isPlayItem(items, index)

                                if (isPlayItem) {
                                    Log.d("MazeComponent", "Clicked in button: $index")
                                    onClick(index)
                                }
                            }
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (topScroll + dragAmount > 0 && topScroll + dragAmount < topY) {
                        topScroll += dragAmount
                    }
                }
            }
    ) {
        translate(top = topScroll) {
            mazePoints.forEachIndexed { index, point ->
                val pointOffset = Offset(point.x, point.y)

                val itemPlayed = items.getOrNull(index)?.played == true
                val isPlayItem = isPlayItem(items, index)

                val circleColor = if (itemPlayed || isPlayItem) colorPrimary else colorSurfaceVariant

                drawCircle(
                    color = circleColor,
                    radius = circleRadius,
                    center = pointOffset
                )

                if (isPlayItem) {
                    drawCircle(
                        color = colorSurface,
                        radius = circleRadius / 1.5f,
                        center = pointOffset
                    )
                }

                val iconPlaySizer = with(localDensity) {
                    Size(30.dp.toPx(), 30.dp.toPx())
                }

                // Value to center the play button in the circle
                val dp15 = with(localDensity) {
                    15.dp.toPx()
                }

                val iconColor = when {
                    !isPlayItem && !itemPlayed -> colorSecondary.copy(alpha = 0.3f)
                    !isPlayItem && itemPlayed -> colorSurface
                    else -> colorPrimary
                }

                translate(
                    left = pointOffset.x - dp15,
                    top = pointOffset.y - dp15
                ) {
                    with(
                        when {
                            !isPlayItem && !itemPlayed -> lockPainter
                            !isPlayItem && itemPlayed -> checkPainter
                            else -> playPainter
                        }
                    ) {
                        draw(
                            size = iconPlaySizer,
                            colorFilter = ColorFilter.tint(iconColor)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@PreviewNightLight
private fun MazeComponentPreview() {
    val completedItems = List(9) {
        MathQuizMaze.MazeItem(
            id = it,
            formula = MathFormula.fromStringFullFormula("1+1=2"),
            difficulty = QuestionDifficulty.Easy,
            played = true
        )
    }

    val otherItems = List(20) {
        MathQuizMaze.MazeItem(
            id = it,
            formula = MathFormula.fromStringFullFormula("1+1=2"),
            difficulty = QuestionDifficulty.Easy
        )
    }

    NewQuizTheme {
        Surface {
            MazeComponent(
                modifier = Modifier.padding(16.dp),
                items = completedItems + otherItems,
                onItemClick = {}
            )
        }
    }
}