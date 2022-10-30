package com.infinitepower.newquiz.online_services.ui.profile.components

import androidx.annotation.StringRes
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
import androidx.compose.ui.tooling.preview.Preview
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.R as CoreR
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private sealed class DayState(@StringRes val text: Int) {
    object Morning : DayState(text = CoreR.string.morning)

    object Afternoon : DayState(text = CoreR.string.afternoon)

    object Evening : DayState(text = CoreR.string.evening)

    object Night : DayState(text = CoreR.string.night)
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
            in 0..11 -> DayState.Morning
            in 12..15 -> DayState.Afternoon
            in 16..20 -> DayState.Evening
            in 21..23 -> DayState.Night
            else -> throw IllegalArgumentException()
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
            append(stringResource(id = CoreR.string.good_day_text, dayText))
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
@Preview(showBackground = true)
private fun GoodDayTextPreview() {
    NewQuizTheme {
        Surface {
            GoodDayText(name = "NewQuiz")
        }
    }
}