package com.infinitepower.newquiz.compose.ui.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.compose.core.theme.NewQuizTheme

data class RoundLineChartData(
    val x: Double,
    val y: Double
)

@Composable
fun RoundLineChart(
    modifier: Modifier = Modifier,
    dataList: List<RoundLineChartData>
) {
    if (dataList.isEmpty()) return

    val listHigherValue = dataList.maxOf { it.y }

    val colorPrimary = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {
        // total chart data
        val totalChartData = dataList.size

        // Maximum distance between dots
        val lineDistance = size.width / (totalChartData + 1)

        // Canvas height
        val cHeight = size.height

        // Add padding for the initial point where line starts
        var currentLineDistance = 0f + lineDistance

        dataList.forEachIndexed { index, data ->
            if (totalChartData >= index + 2) {
                drawLine(
                    start = Offset(
                        x = currentLineDistance,
                        y = calculateYCoordinate(
                            higherDataListValue = listHigherValue,
                            currentDataValue = data.y,
                            canvasHeight = cHeight
                        )
                    ),
                    end = Offset(
                        x = currentLineDistance + lineDistance,
                        y = calculateYCoordinate(
                            higherDataListValue = listHigherValue,
                            currentDataValue = dataList[index + 1].y,
                            canvasHeight = cHeight
                        )
                    ),
                    color = colorPrimary,
                    strokeWidth = 5f
                )
            }
            currentLineDistance += lineDistance
        }
    }
}

@Preview
@Composable
fun RoundLineChartPreview() {
    val dataList = listOf(
        RoundLineChartData(x = 0.0, y = 1.0),
        RoundLineChartData(x = 0.0, y = 2.0),
        RoundLineChartData(x = 0.0, y = 1.3),
        RoundLineChartData(x = 0.0, y = 3.4),
        RoundLineChartData(x = 0.0, y = 2.6),
    )

    NewQuizTheme {
        Surface {
            RoundLineChart(
                dataList = dataList,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}

/**
 * Calculates the Y pixel coordinate for a given transaction rate.
 *
 * @param higherDataListValue the highest rate value in the whole list.
 * @param currentDataValue the current value while iterating the list.
 * @param canvasHeight the canvas HEIGHT for draw the linear chart.
 *
 * @return [Float] Y coordinate for a transaction rate.
 */
private fun calculateYCoordinate(
    higherDataListValue: Double,
    currentDataValue: Double,
    canvasHeight: Float
): Float {
    val maxAndCurrentValueDifference = (higherDataListValue - currentDataValue).toFloat()
    val relativePercentageOfScreen = (canvasHeight / higherDataListValue).toFloat()
    return maxAndCurrentValueDifference * relativePercentageOfScreen
}