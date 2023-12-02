package com.infinitepower.newquiz.core.user_services.ui.profile.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.user_services.XpEarnedByDays
import com.patrykandpatryk.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatryk.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.column.columnChart
import com.patrykandpatryk.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatryk.vico.compose.style.ProvideChartStyle
import com.patrykandpatryk.vico.core.axis.AxisPosition
import com.patrykandpatryk.vico.core.axis.formatter.AxisValueFormatter
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
        chartStyle = m3ChartStyle()
    ) {
        Chart(
            modifier = modifier,
            chart = columnChart(),
            model = chartEntryModel,
            startAxis = rememberStartAxis(
                valueFormatter = { value, _ -> value.toInt().toString() }
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = horizontalAxisValueFormatter
            ),
        )
    }
}

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