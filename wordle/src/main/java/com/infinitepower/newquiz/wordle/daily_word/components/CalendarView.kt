package com.infinitepower.newquiz.wordle.daily_word.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.calendar.CalendarMonthViewImpl
import com.infinitepower.newquiz.core.calendar.MonthDay
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.CustomColor
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.extendedColors
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.compose.StatusWrapper
import com.infinitepower.newquiz.model.wordle.daily.CalendarItemState
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyCalendarItem
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@ExperimentalMaterial3Api
internal fun WordleCalendarView(
    modifier: Modifier = Modifier,
    days: List<MonthDay>,
    firstDayDate: LocalDate?,
    savedCalendarItems: List<WordleDailyCalendarItem>,
    onNextMonthClick: () -> Unit,
    onPreviousMonthClick: () -> Unit,
    onDateClick: (date: LocalDate) -> Unit
) {
    val tz = TimeZone.currentSystemDefault()

    val currentDay = Clock.System.now().toLocalDateTime(tz).date

    val headerText = remember(firstDayDate) {
        if (firstDayDate == null) return@remember ""

        val month = firstDayDate.month.name
        "$month ${firstDayDate.year}"
    }

    WordleCalendarViewImpl(
        modifier = modifier,
        headerText = headerText,
        days = days,
        savedCalendarItems = savedCalendarItems,
        currentDay = currentDay,
        onNextMonthClick = onNextMonthClick,
        onPreviousMonthClick = onPreviousMonthClick,
        onDateClick = onDateClick
    )
}

@Composable
@ExperimentalMaterial3Api
private fun WordleCalendarViewImpl(
    modifier: Modifier = Modifier,
    headerText: String,
    days: List<MonthDay>,
    savedCalendarItems: List<WordleDailyCalendarItem>,
    currentDay: LocalDate,
    onNextMonthClick: () -> Unit,
    onPreviousMonthClick: () -> Unit,
    onDateClick: (date: LocalDate) -> Unit
) {
    Column(modifier = modifier) {
        CalendarHeader(
            headerText = headerText,
            modifier = Modifier.fillMaxWidth(),
            nextMonth = onNextMonthClick,
            backMonth = onPreviousMonthClick
        )

        CalendarWeekWithDays(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            days = days,
            savedCalendarItems = savedCalendarItems,
            currentDay = currentDay,
            onDateClick = onDateClick
        )
    }
}

@Composable
private fun CalendarHeader(
    modifier: Modifier = Modifier,
    headerText: String,
    backMonth: () -> Unit,
    nextMonth: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        HeaderNavigationButton(
            icon = Icons.Rounded.ArrowBack,
            contentDescription = stringResource(id = CoreR.string.back_month),
            onClick = backMonth
        )

        Text(
            text = headerText,
            style = MaterialTheme.typography.titleMedium
        )

        HeaderNavigationButton(
            icon = Icons.Rounded.ArrowForward,
            contentDescription = stringResource(id = CoreR.string.next_month),
            onClick = nextMonth
        )
    }
}

@Composable
private fun HeaderNavigationButton(
    icon: ImageVector,
    contentDescription: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun CalendarWeekWithDays(
    modifier: Modifier = Modifier,
    days: List<MonthDay>,
    savedCalendarItems: List<WordleDailyCalendarItem>,
    currentDay: LocalDate,
    onDateClick: (date: LocalDate) -> Unit
) {
    val weekItems = remember { WeekNameRowItem.allItems }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        items(items = weekItems) { week ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                WeekNameText(text = week.text)
            }
        }

        items(items = days) { day ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (day is MonthDay.Day) {
                    val savedItem = savedCalendarItems.find { it.date == day.date }

                    CalendarItem(
                        day = day.toString(),
                        isCurrentDay = day.date == currentDay,
                        onClick = { onDateClick(day.date) },
                        state = savedItem?.state ?: CalendarItemState.NONE,
                        enabled = day.date <= currentDay
                    )
                }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun WeekNameText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier.padding(MaterialTheme.spacing.small)
    )
}

@Composable
@ExperimentalMaterial3Api
private fun CalendarItem(
    modifier: Modifier = Modifier,
    day: String,
    isCurrentDay: Boolean,
    state: CalendarItemState = CalendarItemState.NONE,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val backgroundColor = when (state) {
        CalendarItemState.NONE -> MaterialTheme.colorScheme.surface
        CalendarItemState.WON -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Green)
        CalendarItemState.LOSS -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Red)
    }

    val textColor = when {
        state == CalendarItemState.WON -> MaterialTheme.extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Green)
        state == CalendarItemState.LOSS -> MaterialTheme.extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Red)
        state == CalendarItemState.NONE && isCurrentDay -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface
    }

    val border = if (isCurrentDay) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    } else null

    StatusWrapper(enabled = enabled) {
        Surface(
            onClick = onClick,
            modifier = modifier.size(35.dp),
            shape = CircleShape,
            color = backgroundColor,
            border = border,
            enabled = enabled
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = textColor
                )
            }
        }
    }
}

private sealed class WeekNameRowItem(
    val text: String // TODO: add locale
) {
    companion object {
        val allItems: List<WeekNameRowItem>
            get() = listOf(Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday)
    }

    object Sunday : WeekNameRowItem(text = "S")

    object Monday : WeekNameRowItem(text = "M")

    object Tuesday : WeekNameRowItem(text = "T")

    object Wednesday : WeekNameRowItem(text = "W")

    object Thursday : WeekNameRowItem(text = "T")

    object Friday : WeekNameRowItem(text = "F")

    object Saturday : WeekNameRowItem(text = "S")
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun CalendarViewPreview() {
    val monthView = CalendarMonthViewImpl()

    val days = monthView.generateCalendarDays()

    val firstDayDate = days
        .filterIsInstance<MonthDay.Day>()
        .firstOrNull()
        ?.date

    NewQuizTheme {
        Surface {
            WordleCalendarView(
                days = days,
                savedCalendarItems = emptyList(),
                onDateClick = {},
                onPreviousMonthClick = {},
                onNextMonthClick = {},
                firstDayDate = firstDayDate
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, group = "Calendar item")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Calendar item")
private fun CalendarItemPreview() {
    val tz = TimeZone.currentSystemDefault()
    val day = remember {
        Clock.System.now().toLocalDateTime(tz).date.dayOfMonth.toString()
    }

    NewQuizTheme {
        Surface {
            Row {
                CalendarItem(
                    day = day,
                    isCurrentDay = false,
                    state = CalendarItemState.NONE,
                    onClick = {},
                    modifier = Modifier.padding(16.dp)
                )
                CalendarItem(
                    day = day,
                    isCurrentDay = true,
                    state = CalendarItemState.NONE,
                    onClick = {},
                    modifier = Modifier.padding(16.dp)
                )
                CalendarItem(
                    day = day,
                    isCurrentDay = false,
                    state = CalendarItemState.WON,
                    onClick = {},
                    modifier = Modifier.padding(16.dp)
                )
                CalendarItem(
                    day = day,
                    isCurrentDay = false,
                    state = CalendarItemState.LOSS,
                    onClick = {},
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}