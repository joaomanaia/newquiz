package com.infinitepower.newquiz.model.math_quiz.maze

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.model.maze.MazePoint
import com.infinitepower.newquiz.model.maze.generateMazePointsBottomToTop
import com.infinitepower.newquiz.model.maze.generateMazePointsTopToBottom
import com.infinitepower.newquiz.model.maze.isInsideCircle
import org.junit.jupiter.api.Test

internal class MazePointTest {
    @Test
    fun `generate maze points, top to bottom test`() {
        val points = generateMazePointsTopToBottom(
            startPoint = MazePoint(50f, 0f),
            increment = MazePoint(50f, 50f)
        ).take(8).toList()

        println(points)

        val expectedPoints = listOf(
            MazePoint(x = 50f, 0f),
            MazePoint(x = 50f, 50f),
            MazePoint(x = 100f, 50f),
            MazePoint(x = 100f, 100f),
            MazePoint(x = 50f, 100f),
            MazePoint(x = 0f, 100f),
            MazePoint(x = 0f, 150f),
            MazePoint(x = 50f, 150f)
        )

        assertThat(points).isEqualTo(expectedPoints)
    }

    @Test
    fun `generate maze points, bottom to top test`() {
        val points = generateMazePointsBottomToTop(
            startPoint = MazePoint(50f, 1000f),
            increment = MazePoint(50f, 50f)
        ).take(9).toList()

        println(points)

        val expectedPoints = listOf(
            MazePoint(x = 50f, 1000f),
            MazePoint(x = 50f, 950f),
            MazePoint(x = 100f, 950f),
            MazePoint(x = 100f, 900f),
            MazePoint(x = 50f, 900f),
            MazePoint(x = 0f, 900f),
            MazePoint(x = 0f, 850f),
            MazePoint(x = 50f, 850f),
            MazePoint(x = 100f, 850f)
        )

        assertThat(points).isEqualTo(expectedPoints)
    }

    @Test
    fun `is inside circle test`() {
        val circlePoint = MazePoint(0f, 0f)

        val testPointInside = MazePoint(1f, 1f)
        val testPointOutside = MazePoint(2f, 2f)

        // Set the radius of the circle
        val radius = 2f

        assertThat(testPointInside.isInsideCircle(circlePoint, radius)).isTrue()
        assertThat(testPointOutside.isInsideCircle(circlePoint, radius)).isFalse()
    }
}
