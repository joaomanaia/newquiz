package com.infinitepower.newquiz.maze_quiz.components

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.infinitepower.newquiz.core.util.collections.indexOfFirstOrNull
import com.infinitepower.newquiz.model.math_quiz.MathFormula
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.maze.MazePoint
import com.infinitepower.newquiz.model.maze.generateMazePointsBottomToTop
import com.infinitepower.newquiz.model.maze.isInsideCircle
import com.infinitepower.newquiz.model.maze.isItemPlayed
import com.infinitepower.newquiz.model.maze.isPlayableItem
import com.infinitepower.newquiz.model.maze.toMazePoint
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import kotlin.math.abs

@Composable
internal fun MazeComponent(
    modifier: Modifier = Modifier,
    items: List<MazeQuiz.MazeItem>,
    onItemClick: (item: MazeQuiz.MazeItem) -> Unit
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

@Composable
private fun MazeComponentImpl(
    modifier: Modifier = Modifier,
    items: List<MazeQuiz.MazeItem>,
    startPoint: MazePoint,
    onClick: (index: Int) -> Unit
) {
    val localDensity = LocalDensity.current

    val colorPrimary = MaterialTheme.colorScheme.primary
    val colorSecondary = MaterialTheme.colorScheme.secondary
    val colorSurface = MaterialTheme.colorScheme.surface
    val colorSurfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    val currentPlayItemIndex = remember(items) {
        items.indexOfFirstOrNull { !it.played }
    }

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

    val height = remember(mazePoints.size) {
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
            .pointerInput(currentPlayItemIndex) {
                detectTapGestures(
                    onTap = { tapOffset ->
                        val tapPoint = tapOffset.toMazePoint()

                        val tapIndex = mazePoints.indexOfFirstOrNull { mazePoint ->
                            val realMazePoint = mazePoint.copy(y = mazePoint.y + topScroll)
                            tapPoint.isInsideCircle(realMazePoint, circleRadius)
                        }

                        if (tapIndex != null) {
                            if (items.isPlayableItem(tapIndex)) onClick(tapIndex)
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

                val itemPlayed = items.isItemPlayed(index)
                val isPlayItem = items.isPlayableItem(index)

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

private fun getContentColor(
    items: List<MazeQuiz.MazeItem>,
    index: Int,
    colorPrimary: Color,
    colorSurfaceVariant: Color
): Color {
    val itemPlayed = items.isItemPlayed(index)
    val isPlayItem = items.isPlayableItem(index)

    return if (itemPlayed || isPlayItem) colorPrimary else colorSurfaceVariant
}

@Composable
@PreviewNightLight
private fun MazeComponentPreview() {
    val completedItems = List(9) {
        MazeQuiz.MazeItem.Wordle(
            word = "1+1=2",
            difficulty = QuestionDifficulty.Easy,
            played = true,
            wordleQuizType = WordleQuizType.MATH_FORMULA,
            mazeSeed = 0
        )
    }

    val otherItems = List(20) {
        MazeQuiz.MazeItem.Wordle(
            word = "1+1=2",
            difficulty = QuestionDifficulty.Easy,
            wordleQuizType = WordleQuizType.MATH_FORMULA,
            mazeSeed = 0
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