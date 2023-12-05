package com.infinitepower.newquiz.core.user_services.ui.profile.components

import android.graphics.Typeface
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.user_services.XpEarnedByDays
import com.infinitepower.newquiz.core.user_services.ui.profile.components.chart.rememberMarker
import com.infinitepower.newquiz.core.R as CoreR
import com.patrykandpatryk.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatryk.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.column.columnChart
import com.patrykandpatryk.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatryk.vico.compose.component.shapeComponent
import com.patrykandpatryk.vico.compose.component.textComponent
import com.patrykandpatryk.vico.compose.dimensions.dimensionsOf
import com.patrykandpatryk.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatryk.vico.compose.style.ProvideChartStyle
import com.patrykandpatryk.vico.core.axis.AxisItemPlacer
import com.patrykandpatryk.vico.core.axis.AxisPosition
import com.patrykandpatryk.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatryk.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatryk.vico.core.component.shape.Shapes
import com.patrykandpatryk.vico.core.entry.entryModelOf
import com.patrykandpatryk.vico.core.entry.entryOf
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.days

@Composable
internal fun XpEarnedByDayCard(
    modifier: Modifier = Modifier,
    xpEarnedByDay: XpEarnedByDays
) {
    val dateTimeFormatter: DateTimeFormatter = remember { DateTimeFormatter.ofPattern("d MMM") }

    val xValuesToDates = xpEarnedByDay.keys.associateBy { it.toEpochDays().toFloat() }
    val chartEntryModel = entryModelOf(xValuesToDates.keys.zip(xpEarnedByDay.values, ::entryOf))
    val horizontalAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        (xValuesToDates[value] ?: LocalDate.fromEpochDays(value.toInt())).toJavaLocalDate().format(dateTimeFormatter)
    }

    ProvideChartStyle(
        chartStyle = m3ChartStyle(
            axisGuidelineColor = MaterialTheme.colorScheme.outlineVariant,
            axisLineColor = MaterialTheme.colorScheme.outlineVariant,
        )
    ) {
        Chart(
            modifier = modifier,
            chart = columnChart(),
            model = chartEntryModel,
            startAxis = rememberStartAxis(
                valueFormatter = { value, _ -> value.toInt().toString() },
                itemPlacer = startAxisItemPlacer,
                horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Outside,
                titleComponent = textComponent(
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

@Composable
@PreviewNightLight
private fun XpThisWeekCardPreview() {
    val now = Clock.System.now()
    val tz = TimeZone.currentSystemDefault()
    val today = now.toLocalDateTime(tz).date

    NewQuizTheme {
        Surface {
            XpEarnedByDayCard(
                modifier = Modifier.padding(16.dp),
                xpEarnedByDay = mapOf(
                    (now - 4.days).toLocalDateTime(tz).date to 20,
                    (now - 3.days).toLocalDateTime(tz).date to 10,
                    (now - 1.days).toLocalDateTime(tz).date to 30,
                    today to 15
                )
            )
        }
    }
}