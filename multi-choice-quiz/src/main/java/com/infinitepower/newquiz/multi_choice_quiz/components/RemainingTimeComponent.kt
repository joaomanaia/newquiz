package com.infinitepower.newquiz.multi_choice_quiz.components

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.model.RemainingTime

@Composable
internal fun RemainingTimeComponent(
    modifier: Modifier = Modifier,
    remainingTime: RemainingTime,
    maxTimeMillis: Long,
    showProgressIndicator: Boolean = true
) {
    val animatedProgressValue by animateFloatAsState(
        targetValue = remainingTime.getRemainingPercent(maxTimeMillis),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "Animated progress value"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        if (showProgressIndicator) {
            CircularProgressIndicator(
                progress = animatedProgressValue,
                modifier = Modifier
                    .size(75.dp)
                    .testTag(RemainingTimeComponentTestTags.PROGRESS_INDICATOR),
                strokeCap = StrokeCap.Round,
                trackColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
            )
        }

        Text(
            text = remainingTime.minuteSecondFormatted(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@VisibleForTesting
internal object RemainingTimeComponentTestTags {
    const val PROGRESS_INDICATOR = "PROGRESS_INDICATOR"
}

@Composable
@PreviewNightLight
private fun RemainingTimeComponentPreview() {
    NewQuizTheme {
        Surface {
            RemainingTimeComponent(
                modifier = Modifier.padding(16.dp),
                remainingTime = RemainingTime.fromMilliseconds(70000),
                maxTimeMillis = 100000L
            )
        }
    }
}