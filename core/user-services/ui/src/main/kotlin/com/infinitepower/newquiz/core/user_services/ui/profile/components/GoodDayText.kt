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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
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

    companion object {
        @Suppress("MagicNumber")
        fun fromHour(hour: Int): DayState {
            return when (hour) {
                in 6..11 -> Morning
                in 12..17 -> Afternoon
                in 18..23 -> Evening
                else -> Night
            }
        }
    }
}

@Composable
internal fun GoodDayText(
    modifier: Modifier = Modifier,
    name: String
) {
    val dayText = remember {
        val tz = TimeZone.currentSystemDefault()
        val now = Clock.System.now()
        val localTime = now.toLocalDateTime(tz)

        DayState.fromHour(localTime.hour)
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
@PreviewLightDark
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
