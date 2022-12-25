package com.infinitepower.newquiz.model.maze

import androidx.annotation.Keep
import androidx.compose.ui.geometry.Offset
import kotlin.math.pow

@Keep
data class MazePoint(
    val x: Float,
    val y: Float
) {
    override fun toString(): String = "x: $x, y: $y"
}

fun Offset.toMazePoint(): MazePoint = MazePoint(x, y)

fun MazePoint.isInsideCircle(
    circlePoint: MazePoint,
    radius: Float
): Boolean = (circlePoint.x - x).pow(2) + (circlePoint.y - y).pow(2) <= radius.pow(2)

/**
 * Creates a maze like this:
 *
 *       ()
 *       |
 *       ()---|
 *            |
 *  |---()---()
 *  |
 *  ()---()---|
 *            |
 *  ()---()---()
 */
fun generateMazePointsTopToBottom(
    startPoint: MazePoint,
    increment: MazePoint
): Sequence<MazePoint> = sequence {
    yield(MazePoint(x = startPoint.x, y = startPoint.y))

    var yValue = startPoint.y + increment.y

    while (true) {
        yield(MazePoint(x = startPoint.x, y = yValue))
        yield(MazePoint(x = startPoint.x + increment.x, y = yValue))
        yValue += increment.y
        yield(MazePoint(x = startPoint.x + increment.x, y = yValue))
        yield(MazePoint(x = startPoint.x, y = yValue))
        yield(MazePoint(x = startPoint.x - increment.x, y = yValue))
        yValue += increment.y
        yield(MazePoint(x = startPoint.x - increment.x, y = yValue))
    }
}

/**
 * Creates a maze like this:
 *
 *  ()---()---()
 *            |
 *  ()---()---|
 *  |
 *  |---()---()
 *           |
 *      ()---|
 *      |
 *     ()
 */
fun generateMazePointsBottomToTop(
    startPoint: MazePoint,
    increment: MazePoint
): Sequence<MazePoint> = sequence {
    yield(MazePoint(x = startPoint.x, y = startPoint.y))

    var yValue = startPoint.y - increment.y

    while (true) {
        yield(MazePoint(x = startPoint.x, y = yValue))
        yield(MazePoint(x = startPoint.x + increment.x, y = yValue))
        yValue -= increment.y
        yield(MazePoint(x = startPoint.x + increment.x, y = yValue))
        yield(MazePoint(x = startPoint.x, y = yValue))
        yield(MazePoint(x = startPoint.x - increment.x, y = yValue))
        yValue -= increment.y
        yield(MazePoint(x = startPoint.x - increment.x, y = yValue))
    }
}