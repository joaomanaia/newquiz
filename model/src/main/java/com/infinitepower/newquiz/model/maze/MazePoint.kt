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

/**
 * Checks if this point is inside the circle defined by [center] and [radius], using the
 * Pythagorean theorem.
 *
 * @param center the center point of the circle
 * @param radius the radius of the circle
 * @return true if this point is inside the circle defined by [center] and [radius]
 */
fun MazePoint.isInsideCircle(
    center: MazePoint,
    radius: Float
): Boolean {
    val dx = x - center.x
    val dy = y - center.y
    return dx.pow(2) + dy.pow(2) <= radius.pow(2)
}

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
