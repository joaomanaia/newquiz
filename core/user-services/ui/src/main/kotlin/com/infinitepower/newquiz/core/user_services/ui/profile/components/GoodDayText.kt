package com.infinitepower.newquiz.core.user_services.ui.profile.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.R as CoreR
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private sealed class DayState(@StringRes val text: Int) {
    data object Morning : DayState(text = CoreR.string.good_morning)

    data object Afternoon : DayState(text = CoreR.string.good_afternoon)

    data object Evening : DayState(text = CoreR.string.good_evening)

    data object Night : DayState(text = CoreR.string.good_night)
}

@Composable
internal fun GoodDayText(
    modifier: Modifier = Modifier,
    name: String
) {
    val localTime = remember {
        val tz = TimeZone.currentSystemDefault()
        val now = Clock.System.now()
        now.toLocalDateTime(tz)
    }

    val dayText = remember(localTime) {
        when (localTime.hour) {
            in 6..11 -> DayState.Morning
            in 12..17 -> DayState.Afternoon
            in 18..23 -> DayState.Evening
            else -> DayState.Night
        }
    }

    GoodDayText(
        modifier = modifier,
        dayText = stringResource(id = dayText.text),
        name = name
    )
}

@Composable
private fun GoodDayText(
    modifier: Modifier = Modifier,
    dayText: String,
    name: String
) {
    Text(
        text = buildAnnotatedString {
            append(dayText)
            append(",\n")
            withStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold)
            ) {
                append(name)
            }
        },
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier
    )
}

@Composable
@PreviewNightLight
private fun GoodDayTextPreview() {
    NewQuizTheme {
        Surface {
            GoodDayText(
                name = "NewQuiz User",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
