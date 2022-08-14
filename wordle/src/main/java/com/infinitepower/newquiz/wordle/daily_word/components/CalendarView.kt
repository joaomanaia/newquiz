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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.CustomColor
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.extendedColors
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.compose.StatusWrapper
import com.infinitepower.newquiz.core.util.calendar.getMonthAllDays
import com.infinitepower.newquiz.model.wordle.daily.CalendarItemState
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyCalendarItem
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun WordleCalendarView(
    modifier: Modifier = Modifier,
    instant: Instant,
    onInstantChanged: (instant: Instant) -> Unit,
    calendarItems: List<WordleDailyCalendarItem>,
    onDateClick: (date: LocalDate) -> Unit
) {
    val tz = TimeZone.currentSystemDefault()

    val currentDay = Clock.System.now().toLocalDateTime(tz).date

    val localDate = remember(instant) {
        instant.toLocalDateTime(tz).date
    }

    val headerText = remember(localDate) {
        val month = localDate.month.name
        "$month ${localDate.year}"
    }

    val days = localDate.getMonthAllDays()

    WordleCalendarViewImpl(
        modifier = modifier,
        headerText = headerText,
        days = days,
        currentDay = currentDay,
        calendarItems = calendarItems,
        backMonth = {
            onInstantChanged(instant.minus(1, DateTimeUnit.MONTH, tz))
        },
        nextMonth = {
            onInstantChanged(instant.plus(1, DateTimeUnit.MONTH, tz))
        },
        onDateClick = onDateClick
    )
}

@Composable
internal fun WordleCalendarView(
    modifier: Modifier = Modifier,
    calendarItems: List<WordleDailyCalendarItem>,
    onDateClick: (date: LocalDate) -> Unit
) {
    val (instant, setInstant) = remember {
        mutableStateOf(Clock.System.now())
    }

    WordleCalendarView(
        modifier = modifier,
        instant = instant,
        onInstantChanged = setInstant,
        calendarItems = calendarItems,
        onDateClick = onDateClick
    )
}

@Composable
@ExperimentalMaterial3Api
private fun WordleCalendarViewImpl(
    modifier: Modifier = Modifier,
    headerText: String,
    days: List<LocalDate>,
    currentDay: LocalDate,
    calendarItems: List<WordleDailyCalendarItem>,
    backMonth: () -> Unit,
    nextMonth: () -> Unit,
    onDateClick: (date: LocalDate) -> Unit
) {
    Column(modifier = modifier) {
        CalendarHeader(
            headerText = headerText,
            modifier = Modifier.fillMaxWidth(),
            backMonth = backMonth,
            nextMonth = nextMonth
        )

        WeekNameRow(modifier = Modifier.fillMaxWidth())

        CalendarDays(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            days = days,
            currentDay = currentDay,
            calendarItems = calendarItems,
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
            contentDescription = "Back month",
            onClick = backMonth
        )

        Text(
            text = headerText,
            style = MaterialTheme.typography.titleMedium
        )

        HeaderNavigationButton(
            icon = Icons.Rounded.ArrowForward,
            contentDescription = "Next month",
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

private sealed class WeekNameRowItem(
    val text: String
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

    object Saturday : WeekNameRowItem(text = "F")
}

@Composable
@ExperimentalMaterial3Api
private fun WeekNameRow(
    modifier: Modifier = Modifier
) {
    val allItems = WeekNameRowItem.allItems

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        allItems.forEach { item ->
            WeekNameText(text = item.text)
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
    day: LocalDate,
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

    val textColor = when (state) {
        CalendarItemState.NONE -> MaterialTheme.colorScheme.onSurface
        CalendarItemState.WON -> MaterialTheme.extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Green)
        CalendarItemState.LOSS -> MaterialTheme.extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Red)
    }

    val border = if (isCurrentDay) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
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
                    text = day.dayOfMonth.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = textColor
                )
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun CalendarDays(
    modifier: Modifier = Modifier,
    days: List<LocalDate>,
    currentDay: LocalDate,
    calendarItems: List<WordleDailyCalendarItem>,
    onDateClick: (date: LocalDate) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        items(items = days) { day ->
            val item = calendarItems.find { it.date == day }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CalendarItem(
                    day = day,
                    isCurrentDay = currentDay == day,
                    onClick = { onDateClick(day) },
                    state = item?.state ?: CalendarItemState.NONE,
                    enabled = day <= currentDay
                )
            }
        }
    }
}

@Composable
@PreviewNightLight
private fun CalendarViewPreview() {
    val calendarItems = listOf(
        WordleDailyCalendarItem(
            date = "2022-08-04".toLocalDate(),
            state = CalendarItemState.LOSS,
            wordSize = 4
        ),
        WordleDailyCalendarItem(
            date = "2022-08-05".toLocalDate(),
            state = CalendarItemState.WON,
            wordSize = 4
        ),
    )

    NewQuizTheme {
        Surface {
            WordleCalendarView(
                calendarItems = calendarItems,
                onDateClick = {}
            )
        }
    }
}

@Composable
@Preview(showBackground = true, group = "Calendar header")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Calendar header")
private fun CalendarHeaderPreview() {
    NewQuizTheme {
        Surface {
            CalendarHeader(
                headerText = "August 2022",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                backMonth = {},
                nextMonth = {}
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
        Clock.System.now().toLocalDateTime(tz).date
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, group = "Calendar days")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Calendar days")
private fun CalendarDaysPreview() {
    val tz = TimeZone.currentSystemDefault()

    val currentDay = Clock.System.now().toLocalDateTime(tz).date

    val localDate = remember {
        Clock.System.now().toLocalDateTime(tz).date
    }

    val days = localDate.getMonthAllDays()

    NewQuizTheme {
        Surface {
            CalendarDays(
                days = days,
                currentDay = currentDay,
                calendarItems = emptyList(),
                onDateClick = {}
            )
        }
    }
}