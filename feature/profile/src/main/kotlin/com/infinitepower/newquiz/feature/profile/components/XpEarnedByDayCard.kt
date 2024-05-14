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
import com.infinitepower.newquiz.core.user_services.TimeRange
import com.infinitepower.newquiz.core.user_services.XpEarnedByDateTime
import com.infinitepower.newquiz.feature.profile.components.chart.rememberMarker
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

@Composable
internal fun XpEarnedByDayCard(
    modifier: Modifier = Modifier,
    timeRange: TimeRange,
    xpEarnedByDay: XpEarnedByDateTime
) {
    val xValuesToDates = remember(xpEarnedByDay) {
        xpEarnedByDay.keys.associateBy { it.toFloat() }
    }
    val chartEntryModel = remember(xValuesToDates, xpEarnedByDay) {
        entryModelOf(xValuesToDates.keys.zip(xpEarnedByDay.values, ::entryOf))
    }
    val horizontalAxisValueFormatter = remember(timeRange, xValuesToDates) {
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
            val data = xValuesToDates[value] ?: value.toInt()

            timeRange.formatValueToString(data)
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
            ),
            bottomAxis = rememberBottomAxis(
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
private fun XpTodayCardPreview() {
    val now = Clock.System.now()
    val tz = TimeZone.currentSystemDefault()

    NewQuizTheme {
        Surface {
            XpEarnedByDayCard(
                modifier = Modifier.padding(16.dp),
                timeRange = TimeRange.Today,
                xpEarnedByDay = mapOf(
                    (now - 4.hours).toLocalDateTime(tz).hour to 10,
                    (now - 3.hours).toLocalDateTime(tz).hour to 5,
                    now.toLocalDateTime(tz).hour to 15
                )
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun XpThisWeekCardPreview() {
    val now = Clock.System.now()
    val tz = TimeZone.currentSystemDefault()
    val today = now.toLocalDateTime(tz).date

    NewQuizTheme {
        Surface {
            XpEarnedByDayCard(
                modifier = Modifier.padding(16.dp),
                timeRange = TimeRange.ThisWeek,
                xpEarnedByDay = mapOf(
                    (now - 4.days).toLocalDateTime(tz).date.toEpochDays() to 20,
                    (now - 3.days).toLocalDateTime(tz).date.toEpochDays() to 10,
                    (now - 1.days).toLocalDateTime(tz).date.toEpochDays() to 30,
                    today.toEpochDays() to 15
                )
            )
        }
    }
}
