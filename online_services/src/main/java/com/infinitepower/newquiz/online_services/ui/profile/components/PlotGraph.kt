package com.infinitepower.newquiz.online_services.ui.profile.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme

@Composable
fun CubicChart(
    modifier: Modifier = Modifier,
    yPoints: List<Float> = emptyList(),
    graphColor: Color = MaterialTheme.colorScheme.primaryContainer,
    pointColor: Color = MaterialTheme.colorScheme.primary,
) {
    val spacing = 100f

    Box(
        modifier = modifier.padding(all = 16.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            drawRect(
                color = graphColor,
                topLeft = Offset.Zero,
                size = Size(
                    width = size.width,
                    height = size.height
                ),
                style = Stroke()
            )

            val spacePerHour = (size.width - spacing) / yPoints.size

            val normX = mutableListOf<Float>()
            val normY = mutableListOf<Float>()

            val strokePath = Path().apply {
                for (i in yPoints.indices) {
                    val currentX = spacing + i * spacePerHour

                    if (i == 0) {
                        moveTo(currentX, yPoints[i])
                    } else {
                        val previousX = spacing + (i - 1) * spacePerHour

                        val conX1 = (previousX + currentX) / 2f
                        val conX2 = (previousX + currentX) / 2f

                        val conY1 = yPoints[i - 1]
                        val conY2 = yPoints[i]

                        cubicTo(
                            x1 = conX1,
                            y1 = conY1,
                            x2 = conX2,
                            y2 = conY2,
                            x3 = currentX,
                            y3 = yPoints[i]
                        )
                    }

                    // Circle dot points
                    normX.add(currentX)
                    normY.add(yPoints[i])
                }
            }

            drawPath(
                path = strokePath,
                color = graphColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            (normX.indices).forEach {
                drawCircle(
                    pointColor,
                    radius = 3.dp.toPx(),
                    center = Offset(normX[it], normY[it])
                )
            }
        }
    }
}

@Composable
@PreviewNightLight
private fun PlotGraphPreview() {
    val points = listOf(
        199f, 52f, 193f, 290f, 150f, 445f
    )
    //val points = listOf(100f, 230f, 170f, 270f, 210f)

    NewQuizTheme {
        Surface {
            CubicChart(
                modifier = Modifier.fillMaxWidth(),
                yPoints = points,
            )
        }
    }
}