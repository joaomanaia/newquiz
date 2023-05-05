package com.infinitepower.newquiz.daily_challenge.components

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import kotlinx.datetime.Instant

@Composable
@ExperimentalMaterial3Api
internal fun DailyChallengeCard(
    modifier: Modifier = Modifier,
    now: Instant,
    title: String,
    currentValue: UInt,
    maxValue: UInt,
    dateRange: ClosedRange<Instant>,
    isCompleted: Boolean,
    isClaimed: Boolean,
    onClaimClick: () -> Unit,
    onCardClick: () -> Unit
) {
    val isExpired = remember(dateRange, now) {
        now > dateRange.endInclusive
    }

    val remainingTime = remember(dateRange, now) {
        val remaining = dateRange.endInclusive - now

        if (remaining.isNegative()) {
            "0"
        } else {
            DateUtils.formatElapsedTime(remaining.inWholeSeconds)
        }
    }

    DailyChallengeCard(
        modifier = modifier,
        title = title,
        currentValue = currentValue,
        maxValue = maxValue,
        remainingTime = remainingTime,
        isCompleted = isCompleted,
        enabled = !isExpired && !isClaimed,
        onClaimClick = onClaimClick,
        onCardClick = onCardClick
    )
}

@Composable
@ExperimentalMaterial3Api
internal fun DailyChallengeCard(
    modifier: Modifier = Modifier,
    title: String,
    currentValue: UInt,
    maxValue: UInt,
    remainingTime: String,
    isCompleted: Boolean,
    enabled: Boolean = true,
    onClaimClick: () -> Unit,
    onCardClick: () -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val progress = remember(currentValue, maxValue) {
        currentValue.toFloat() / maxValue.toFloat()
    }

    val progressColor = if (enabled) {
        ProgressIndicatorDefaults.linearColor
    } else {
        ProgressIndicatorDefaults.linearColor.copy(alpha = 0.3f)
    }

    val trackColor = if (enabled) {
        ProgressIndicatorDefaults.linearTrackColor
    } else {
        ProgressIndicatorDefaults.linearTrackColor.copy(alpha = 0.3f)
    }

    OutlinedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        onClick = onCardClick,
        enabled = enabled
    ) {
        Column(
            modifier = Modifier.padding(spaceMedium)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(spaceMedium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Remaining: $remainingTime",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "$currentValue / $maxValue",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(spaceMedium))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth(),
                strokeCap = StrokeCap.Round,
                color = progressColor,
                trackColor = trackColor
            )
            if (isCompleted && enabled) {
                Spacer(modifier = Modifier.height(spaceMedium))
                Button(
                    onClick = onClaimClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Claim")
                }
            }
        }
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun DailyChallengeCardPreview() {
    NewQuizTheme {
        Surface {
            DailyChallengeCard(
                modifier = Modifier.padding(16.dp),
                title = "Daily Challenge",
                currentValue = 7u,
                maxValue = 10u,
                remainingTime = "60s",
                isCompleted = false,
                onClaimClick = {},
                onCardClick = {}
            )
        }
    }
}