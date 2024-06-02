package com.infinitepower.newquiz.feature.profile.components

import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.user_services.DateTimeRangeFormatter
import com.infinitepower.newquiz.feature.profile.components.chart.rememberMarker
import com.infinitepower.newquiz.model.TimestampWithXP
import com.patrykandpatryk.vico.compose.axis.axisGuidelineComponent
import com.patrykandpatryk.vico.compose.axis.axisLabelComponent
import com.infinitepower.newquiz.core.R as CoreR
import com.patrykandpatryk.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatryk.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.column.columnChart
import com.patrykandpatryk.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatryk.vico.compose.component.shapeComponent
import com.patrykandpatryk.vico.compose.component.textComponent as composeTextComponent
import com.patrykandpatryk.vico.compose.dimensions.dimensionsOf
import com.patrykandpatryk.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatryk.vico.compose.style.ProvideChartStyle
import com.patrykandpatryk.vico.core.axis.AxisItemPlacer
import com.patrykandpatryk.vico.core.axis.AxisPosition
import com.patrykandpatryk.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatryk.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatryk.vico.core.chart.decoration.Decoration
import com.patrykandpatryk.vico.core.chart.draw.ChartDrawContext
import com.patrykandpatryk.vico.core.component.shape.Shapes
import com.patrykandpatryk.vico.core.component.text.TextComponent
import com.patrykandpatryk.vico.core.component.text.textComponent
import com.patrykandpatryk.vico.core.entry.entryModelOf
import com.patrykandpatryk.vico.core.entry.entryOf
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

@Composable
internal fun XpEarnedByDayCard(
    modifier: Modifier = Modifier,
    formatter: DateTimeRangeFormatter,
    xpEarnedList: List<TimestampWithXP>
) {
    val resultsAggregated = remember(formatter, xpEarnedList) {
        formatter.aggregateResults(xpEarnedList)
    }

    val xValuesToDates = remember(resultsAggregated) {
        resultsAggregated.keys.associateBy { it.toFloat() }
    }
    val chartEntryModel = remember(xValuesToDates, resultsAggregated) {
        entryModelOf(xValuesToDates.keys.zip(resultsAggregated.values, ::entryOf))
    }
    val horizontalAxisValueFormatter = remember(formatter, xValuesToDates) {
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
            val data = xValuesToDates[value] ?: value.toInt()

            formatter.formatValueToString(data)
        }
    }

    val noDataTextComponent = composeTextComponent(
        color = MaterialTheme.colorScheme.onSurface,
        textSize = NO_DATA_TEXT_SIZE
    )
    val noDataText = stringResource(CoreR.string.no_data)

    val decorations = remember(xValuesToDates) {
        if (xValuesToDates.isEmpty()) {
            listOf(
                NoDataText(
                    text = noDataText,
                    textComponent = noDataTextComponent
                ),
            )
        } else {
            emptyList()
        }
    }

    ProvideChartStyle(
        chartStyle = m3ChartStyle(
            axisGuidelineColor = MaterialTheme.colorScheme.outlineVariant,
            axisLineColor = MaterialTheme.colorScheme.outlineVariant,
        )
    ) {
        Chart(
            modifier = modifier,
            chart = columnChart(decorations = decorations),
            model = chartEntryModel,
            startAxis = rememberStartAxis(
                label = if (resultsAggregated.isEmpty()) null else axisLabelComponent(),
                valueFormatter = { value, _ -> value.toInt().toString() },
                itemPlacer = startAxisItemPlacer,
                horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Outside,
                titleComponent = composeTextComponent(
                    color = MaterialTheme.colorScheme.onPrimary,
                    background = shapeComponent(
                        shape = Shapes.pillShape,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    padding = axisTitlePadding,
                    margins = startAxisTitleMargins,
                    typeface = Typeface.MONOSPACE,
                ),
                title = stringResource(CoreR.string.xp),
                guideline = if (resultsAggregated.isEmpty()) null else axisGuidelineComponent()
            ),
            bottomAxis = rememberBottomAxis(
                label = if (resultsAggregated.isEmpty()) null else axisLabelComponent(),
                valueFormatter = horizontalAxisValueFormatter,
                guideline = null
            ),
            marker = rememberMarker(),
            fadingEdges = rememberFadingEdges(),
        )
    }
}

private class NoDataText(
    val text: String,
    val textComponent: TextComponent = textComponent(),
) : Decoration {
    override fun onDrawAboveChart(context: ChartDrawContext, bounds: RectF) {
        textComponent.drawText(
            context = context,
            text = text,
            textX = bounds.centerX(),
            textY = bounds.centerY(),
        )
    }
}

private const val MAX_START_AXIS_ITEM_COUNT = 6
private val startAxisItemPlacer = AxisItemPlacer.Vertical.default(MAX_START_AXIS_ITEM_COUNT)

private val axisTitleHorizontalPaddingValue = 8.dp
private val axisTitleVerticalPaddingValue = 2.dp
private val axisTitlePadding = dimensionsOf(
    horizontal = axisTitleHorizontalPaddingValue,
    vertical = axisTitleVerticalPaddingValue
)
private val axisTitleMarginValue = 4.dp
private val startAxisTitleMargins = dimensionsOf(end = axisTitleMarginValue)

private val NO_DATA_TEXT_SIZE = 18.sp

@Composable
@PreviewLightDark
private fun EmptyXpCardPreview() {
    NewQuizTheme {
        Surface {
            XpEarnedByDayCard(
                modifier = Modifier.padding(16.dp),
                formatter = DateTimeRangeFormatter.Day,
                xpEarnedList = emptyList()
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun XpTodayCardPreview() {
    val now = Clock.System.now()

    NewQuizTheme {
        Surface {
            XpEarnedByDayCard(
                modifier = Modifier.padding(16.dp),
                formatter = DateTimeRangeFormatter.Day,
                xpEarnedList = listOf(
                    TimestampWithXP((now - 4.hours).toEpochMilliseconds(), 10),
                    TimestampWithXP((now - 3.hours).toEpochMilliseconds(), 5),
                    TimestampWithXP(now.toEpochMilliseconds(), 15)
                )
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun XpThisWeekCardPreview() {
    val now = Clock.System.now()

    NewQuizTheme {
        Surface {
            XpEarnedByDayCard(
                modifier = Modifier.padding(16.dp),
                formatter = DateTimeRangeFormatter.Week,
                xpEarnedList = listOf(
                    TimestampWithXP((now - 4.days).toEpochMilliseconds(), 20),
                    TimestampWithXP((now - 3.days).toEpochMilliseconds(), 10),
                    TimestampWithXP((now - 1.days).toEpochMilliseconds(), 30),
                    TimestampWithXP(now.toEpochMilliseconds(), 15)
                )
            )
        }
    }
}
