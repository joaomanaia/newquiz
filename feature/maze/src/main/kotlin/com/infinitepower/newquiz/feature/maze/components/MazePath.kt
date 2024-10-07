package com.infinitepower.newquiz.feature.maze.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.util.collections.indexOfFirstOrNull
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.maze.isItemPlayed
import com.infinitepower.newquiz.model.maze.isPlayableItem
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleWord
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun MazePath(
    modifier: Modifier = Modifier,
    items: ImmutableList<MazeQuiz.MazeItem>,
    startScrollToCurrentItem: Boolean = true,
    colors: MazeColors = MazeDefaults.defaultColors(),
    horizontalPadding: Dp = MazeDefaults.horizontalPadding,
    verticalPadding: Dp = MazeDefaults.verticalPadding,
    mazeSeed: Int = 0,
    onItemClick: (item: MazeQuiz.MazeItem) -> Unit = {},
) {
    val random = remember(mazeSeed) { Random(mazeSeed) }

    val localDensity = LocalDensity.current
    val horizontalPaddingPx = with(localDensity) { horizontalPadding.toPx() }
    val verticalPaddingPx = with(localDensity) { verticalPadding.toPx() }

    val yPointsSize = items.size

    // Find the index of the current play item.
    val currentPlayItemIndex = remember(items) {
        items.indexOfFirstOrNull { !it.played }
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val screenHeight = constraints.maxHeight
        val screenWidth = constraints.maxWidth

        val points: List<Offset> = remember(yPointsSize) {
            List(yPointsSize) { i ->
                // Get a random number between 2 and 5 to make the horizontal offset of the points more random
                val r = random.nextDouble(
                    from = HorizontalOffsetRandomStart,
                    until = HorizontalOffsetRandomEnd
                ).toFloat()

                val horizontalWidth = screenWidth - horizontalPaddingPx
                val x = sin((i.toFloat() / 2) * PI) * horizontalWidth / r + screenWidth / 2
                val y = localDensity.getPointY(
                    height = screenHeight.toFloat(),
                    index = i,
                    verticalPaddingPx = verticalPaddingPx,
                )

                Offset(x.toFloat(), y)
            }
        }

        // Calculate the height of the graph based on the number of points.
        val graphHeight = remember(yPointsSize) {
            with(localDensity) { PointSpacing.toPx() * (yPointsSize - 1) } + 2 * verticalPaddingPx
        }

        var topScroll by remember { mutableFloatStateOf(0f) }
        val topScrollAnimated = animateFloatAsState(
            targetValue = topScroll,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "Top Scroll"
        )

        val playPainter = rememberVectorPainter(image = Icons.Rounded.PlayArrow)
        val playedPainter = rememberVectorPainter(image = Icons.Rounded.Check)
        val lockPainter = rememberVectorPainter(image = Icons.Rounded.Lock)

        var pressedOffset by remember { mutableStateOf(Offset.Zero) }

        val scrollToCurrentItem: () -> Unit = {
            currentPlayItemIndex?.let { index ->
                // Get the current play item's y position.
                val currentPlayItemY = points[index].y
                // Get the height of the screen divided by 2.
                // This is used to center the current play item on the screen.
                val halfScreenHeight = screenHeight / 2

                val newTopScroll = -currentPlayItemY + halfScreenHeight

                // If the top scroll is less than 0, it means that the current play item is
                // not above the screen. In this case, we don't need to scroll.
                if (newTopScroll >= 0) {
                    val newGraphHeight = graphHeight - newTopScroll

                    topScroll = if (newGraphHeight >= screenHeight) {
                        newTopScroll
                    } else {
                        graphHeight - screenHeight
                    }
                } else {
                    topScroll = 0f
                }
            }
        }

        val visibleScreenHeightRange = remember(topScroll, graphHeight) {
            graphHeight - topScroll - screenHeight..graphHeight - topScroll
        }

        // The scroll button is visible if the current play item is not visible in the screen.
        val scrollButtonState = remember(currentPlayItemIndex, visibleScreenHeightRange) {
            if (currentPlayItemIndex == null) {
                ScrollButtonState.HIDDEN
            } else {
                val currentPlayItemY = points[currentPlayItemIndex].y + graphHeight - screenHeight
                if (currentPlayItemY !in visibleScreenHeightRange) {
                    if (currentPlayItemY < visibleScreenHeightRange.start) {
                        ScrollButtonState.SCROLL_TO_TOP
                    } else {
                        ScrollButtonState.SCROLL_TO_BOTTOM
                    }
                } else {
                    ScrollButtonState.HIDDEN
                }
            }
        }

        ScrollToCurrentQuestionButton(
            modifier = Modifier
                .zIndex(1f)
                .align(Alignment.BottomEnd)
                .padding(MaterialTheme.spacing.medium),
            state = scrollButtonState,
            onClick = scrollToCurrentItem
        )

        // If the startScrollToCurrentItem is true, scroll to the current item.
        LaunchedEffect(key1 = Unit) {
            if (startScrollToCurrentItem) {
                scrollToCurrentItem()
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(graphHeight.dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        val newTopScroll = topScroll + dragAmount
                        val newGraphHeight = graphHeight - newTopScroll

                        if (newTopScroll >= 0 && newGraphHeight >= screenHeight) {
                            topScroll += dragAmount
                        }
                    }
                }
                .pointerInput(currentPlayItemIndex) {
                    detectTapGestures(
                        onTap = { tapOffset ->
                            // Find the index of the item that was tapped
                            val tapIndex = points.indexOfFirstOrNull { point ->
                                val realMazePoint = point.copy(y = point.y + topScroll)
                                tapOffset.isInsideCircle(realMazePoint, CircleSize.toPx())
                            }

                            if (tapIndex != null && tapIndex in items.indices) {
                                if (items.isPlayableItem(tapIndex) || items.isItemPlayed(tapIndex)) {
                                    onItemClick(items[tapIndex])
                                }
                            }
                        },
                        onPress = { pressOffset ->
                            pressedOffset = pressOffset.copy(
                                y = pressOffset.y - topScroll
                            )
                            awaitRelease()
                            pressedOffset = Offset.Zero
                        }
                    )
                }
        ) {
            val completedPath = Path()
            val remainingPath = Path()

            points.forEachIndexed { i, point ->
                val currentY = point.y

                val path =
                    if (items.isItemPlayed(i) || items.isPlayableItem(i)) completedPath else remainingPath

                val isPlayItem = items.isPlayableItem(i - 1)

                val previousY = getPointY(
                    height = size.height,
                    index = i - 1,
                    verticalPaddingPx = verticalPaddingPx,
                )

                if (i == 0) {
                    path.moveTo(size.width / 2, currentY)
                } else {
                    if (isPlayItem) {
                        path.moveTo(
                            x = points[i - 1].x,
                            y = previousY
                        )
                    }

                    val conY1 = PathSmoothness * previousY + (1 - PathSmoothness) * currentY
                    val conY2 = PathSmoothness * currentY + (1 - PathSmoothness) * previousY

                    val conX1 = points[i - 1].x
                    val conX2 = points[i].x

                    path.cubicTo(
                        x1 = conX1,
                        y1 = conY1,
                        x2 = conX2,
                        y2 = conY2,
                        x3 = points[i].x,
                        y3 = currentY
                    )
                }
            }

            translate(top = topScrollAnimated.value) {
                // Draw the path for the completed items
                drawMazePath(
                    path = completedPath,
                    color = colors.pathColor(played = true),
                )

                // Draw the path for the remaining items
                drawMazePath(
                    path = remainingPath,
                    color = colors.pathColor(played = false),
                )

                // Draw the points
                drawPoints(
                    points = points,
                    items = items,
                    colors = colors,
                    pressedOffset = pressedOffset,
                    lockPainter = lockPainter,
                    playPainter = playPainter,
                    playedPainter = playedPainter,
                )
            }
        }
    }
}

private fun DrawScope.drawMazePath(
    path: Path,
    color: Color,
) {
    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = LineSize.toPx(),
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(MazePathEffectInterval)
        ),
    )
}

private fun DrawScope.drawPoints(
    points: List<Offset>,
    items: ImmutableList<MazeQuiz.MazeItem>,
    colors: MazeColors,
    pressedOffset: Offset,
    lockPainter: VectorPainter,
    playPainter: VectorPainter,
    playedPainter: VectorPainter,
) {
    points.forEachIndexed { itemIndex, pointOffset ->
        val itemPlayed = items.isItemPlayed(itemIndex)
        val isPlayableItem = items.isPlayableItem(itemIndex)

        val canPress = isPlayableItem || itemPlayed
        val isPressed = canPress && pressedOffset.isInsideCircle(
            pointOffset,
            CircleSize.toPx()
        )

        scale(
            scale = if (isPressed) CirclePressScale else 1f,
            pivot = pointOffset
        ) {
            val currentItemRoundedPolygon = RoundedPolygon(
                numVertices = ITEM_POLYGON_NUM_VERTICES,
                radius = CircleSize.toPx(),
                centerX = pointOffset.x,
                centerY = pointOffset.y,
                rounding = CornerRounding(
                    radius = size.minDimension / ITEM_POLYGON_RADIUS_PERCENTAGE,
                    smoothing = 0.1f
                )
            )
            val roundedPolygonPath = currentItemRoundedPolygon.toPath().asComposePath()

            drawPath(
                path = roundedPolygonPath,
                color = colors.circleContainerColor(
                    played = itemPlayed,
                    isPlayItem = isPlayableItem
                ),
            )

            if (isPlayableItem) {
                drawCircle(
                    color = colors.currentCircleInnerColor(),
                    radius = CircleInnerSize.toPx(),
                    center = pointOffset
                )
            }

            translate(
                left = pointOffset.x - IconSize.toPx() / 2,
                top = pointOffset.y - IconSize.toPx() / 2
            ) {
                with(
                    when {
                        !isPlayableItem && !itemPlayed -> lockPainter // Locked
                        !isPlayableItem && itemPlayed -> playedPainter // Played
                        else -> playPainter
                    }
                ) {
                    draw(
                        size = Size(IconSize.toPx(), IconSize.toPx()),
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                            colors.circleContentColor(
                                played = itemPlayed,
                                isPlayItem = isPlayableItem
                            )
                        )
                    )
                }
            }
        }
    }
}

/**
 * Returns true if the [Offset] is inside the circle with the given [center] and [radius].
 */
private fun Offset.isInsideCircle(
    center: Offset,
    radius: Float
): Boolean {
    val dx = x - center.x
    val dy = y - center.y
    return dx.pow(2) + dy.pow(2) <= radius.pow(2)
}

private fun Density.getPointY(
    height: Float,
    index: Int,
    verticalPaddingPx: Float,
): Float {
    return height - index * PointSpacing.toPx() - verticalPaddingPx
}

object MazeDefaults {
    /**
     * The horizontal padding applied to the graph.
     */
    val horizontalPadding: Dp @Composable get() = 120.dp

    /**
     * The vertical padding applied to the graph.
     */
    val verticalPadding: Dp @Composable get() = MaterialTheme.spacing.extraLarge

    @Composable
    fun defaultColors(
        playedPathColor: Color = MaterialTheme.colorScheme.primary,
        playedCircleContainerColor: Color = MaterialTheme.colorScheme.primary,
        playedCircleContentColor: Color = MaterialTheme.colorScheme.onPrimary,
        lockedPathColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f),
        lockedCircleContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        lockedCircleContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
        currentCircleContainerColor: Color = playedCircleContainerColor,
        currentCircleInnerColor: Color = MaterialTheme.colorScheme.surface,
        currentCircleContentColor: Color = currentCircleContainerColor,
    ): MazeColors = MazeColors(
        playedPathColor = playedPathColor,
        playedCircleContainerColor = playedCircleContainerColor,
        playedCircleContentColor = playedCircleContentColor,
        lockedPathColor = lockedPathColor,
        lockedCircleContainerColor = lockedCircleContainerColor,
        lockedCircleContentColor = lockedCircleContentColor,
        currentCircleContainerColor = currentCircleContainerColor,
        currentCircleInnerColor = currentCircleInnerColor,
        currentCircleContentColor = currentCircleContentColor,
    )
}

@Immutable
class MazeColors internal constructor(
    private val playedPathColor: Color,
    private val playedCircleContainerColor: Color,
    private val playedCircleContentColor: Color,
    private val lockedPathColor: Color,
    private val lockedCircleContainerColor: Color,
    private val lockedCircleContentColor: Color,
    private val currentCircleInnerColor: Color,
    private val currentCircleContainerColor: Color,
    private val currentCircleContentColor: Color,
) {
    internal fun pathColor(played: Boolean): Color {
        return if (played) playedPathColor else lockedPathColor
    }

    internal fun circleContainerColor(
        played: Boolean,
        isPlayItem: Boolean,
    ): Color {
        return when {
            !isPlayItem && !played -> lockedCircleContainerColor
            !isPlayItem && played -> playedCircleContainerColor
            else -> currentCircleContainerColor
        }
    }

    internal fun circleContentColor(
        played: Boolean,
        isPlayItem: Boolean,
    ): Color = when {
        !isPlayItem && !played -> lockedCircleContentColor
        !isPlayItem && played -> playedCircleContentColor
        else -> currentCircleContentColor
    }

    internal fun currentCircleInnerColor(): Color {
        return currentCircleInnerColor
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MazeColors) return false

        if (playedPathColor != other.playedPathColor) return false
        if (lockedPathColor != other.lockedPathColor) return false
        if (playedCircleContainerColor != other.playedCircleContainerColor) return false
        if (lockedCircleContainerColor != other.lockedCircleContainerColor) return false
        if (playedCircleContentColor != other.playedCircleContentColor) return false
        if (lockedCircleContentColor != other.lockedCircleContentColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = playedPathColor.hashCode()
        result = 31 * result + lockedPathColor.hashCode()
        result = 31 * result + playedCircleContainerColor.hashCode()
        result = 31 * result + lockedCircleContainerColor.hashCode()
        result = 31 * result + playedCircleContentColor.hashCode()
        result = 31 * result + lockedCircleContentColor.hashCode()
        return result
    }
}

private val PointSpacing = 100.dp
private val CircleSize = 35.dp
private const val CIRCLE_INNER_SIZE_PERCENTAGE = 0.6f
private val CircleInnerSize = CircleSize * CIRCLE_INNER_SIZE_PERCENTAGE
private val IconSize = 30.dp
private val LineSize = 12.dp

/**
 * The smoothness of the path.
 */
private const val PathSmoothness = 0.25f

private const val CirclePressScale = 1.1f

private const val ITEM_POLYGON_NUM_VERTICES = 6
private const val ITEM_POLYGON_RADIUS_PERCENTAGE = 30f

private const val MazePathEffectWidth = 50f
private const val MazePathEffectSpacing = 50f
private val MazePathEffectInterval = floatArrayOf(MazePathEffectWidth, MazePathEffectSpacing)

/**
 * The range of the horizontal offset of the points.
 */
private const val HorizontalOffsetRandomStart = 2.0
private const val HorizontalOffsetRandomEnd = 5.0

@Composable
@PreviewLightDark
private fun MazeComponentPreview() {
    val completedItems = List(4) {
        MazeQuiz.MazeItem.Wordle(
            wordleWord = WordleWord("1+1=2"),
            difficulty = QuestionDifficulty.Easy,
            played = true,
            wordleQuizType = WordleQuizType.MATH_FORMULA,
            mazeSeed = 0
        )
    }

    val otherItems = List(8) {
        MazeQuiz.MazeItem.Wordle(
            wordleWord = WordleWord("1+1=2"),
            difficulty = QuestionDifficulty.Easy,
            wordleQuizType = WordleQuizType.MATH_FORMULA,
            mazeSeed = 0
        )
    }

    val items = (completedItems + otherItems).toPersistentList()

    NewQuizTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                MazePath(
                    items = items,
                    startScrollToCurrentItem = false,
                )
            }
        }
    }
}
