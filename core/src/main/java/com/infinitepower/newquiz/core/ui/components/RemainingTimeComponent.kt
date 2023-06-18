package com.infinitepower.newquiz.core.ui.components

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.model.RemainingTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Component that displays the remaining time of a quiz.
 *
 * @param modifier Modifier to be applied to the component.
 * @param remainingTime The remaining time to be displayed.
 * @param maxTime The maximum time of the quiz.
 * @param warningTime The time at which the text starts blinking.
 * @param showProgressIndicator Whether to show the progress indicator or not.
 */
@Composable
fun RemainingTimeComponent(
    modifier: Modifier = Modifier,
    remainingTime: RemainingTime,
    maxTime: Duration,
    warningTime: Duration = 10.seconds,
    showProgressIndicator: Boolean = true,
    animationsEnabled: Boolean = true
) {
    val animatedProgressValue by animateFloatAsState(
        targetValue = remainingTime.getRemainingPercent(maxTime).toFloat(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "Animated progress value"
    )

    // If the remaining time is less than the warning time, the text starts animating.
    val isWarningTime by remember(remainingTime) {
        derivedStateOf {
            animationsEnabled && remainingTime.value <= warningTime
        }
    }

    val progressColor by animateColorAsState(
        targetValue = if (isWarningTime) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.primary
        },
        label = "Animated progress color"
    )

    val trackProgressColor by animateColorAsState(
        targetValue = if (isWarningTime) {
            MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
        } else {
            MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
        },
        label = "Animated track progress color"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        if (showProgressIndicator) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(75.dp)
                    .testTag(RemainingTimeComponentTestTags.PROGRESS_INDICATOR),
                progress = animatedProgressValue,
                strokeCap = StrokeCap.Round,
                color = progressColor,
                trackColor = trackProgressColor,
            )
        }

        if (isWarningTime) {
            AnimatedRemainingTimeText(remainingTime = remainingTime)
        } else {
            RemainingTimeText(remainingTime = remainingTime.toMinuteSecondFormatted())
        }

    }
}

/**
 * Component that displays the text of the remaining time of a quiz.
 *
 * @param modifier Modifier to be applied to the component.
 */
@Composable
private fun RemainingTimeText(
    modifier: Modifier = Modifier,
    remainingTime: String,
    style: TextStyle = MaterialTheme.typography.titleMedium,
    color: Color = LocalContentColor.current
) {
    Text(
        text = remainingTime,
        textAlign = TextAlign.Center,
        style = style,
        modifier = modifier,
        color = color
    )
}

@Composable
private fun AnimatedRemainingTimeText(
    modifier: Modifier = Modifier,
    remainingTime: RemainingTime
) {
    val remainingTimeInSeconds by remember(remainingTime) {
        derivedStateOf {
            remainingTime.value.inWholeSeconds
        }
    }

    RemainingTimeText(
        modifier = modifier,
        remainingTime = remainingTimeInSeconds.toString(),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.error
    )

    /* Crash problem?

    AnimatedContent(
        modifier = modifier,
        targetState = remainingTimeInSeconds,
        transitionSpec = {
            // Compare the incoming number with the previous number.
            if (remainingTimeInSeconds > initialState) {
                // If the target number is larger, it slides up and fades in
                // while the initial (smaller) number slides up and fades out.
                slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
            } else {
                // If the target number is smaller, it slides down and fades in
                // while the initial number slides down and fades out.
                slideInVertically { height -> -height } + fadeIn() togetherWith
                        slideOutVertically { height -> height } + fadeOut()
            }.using(
                // Disable clipping since the faded slide-in/out should
                // be displayed out of bounds.
                SizeTransform(clip = false)
            )
        },
        label = "Pulsating remaining time text"
    ) { targetRemainingTime ->
        RemainingTimeText(
            remainingTime = targetRemainingTime.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )
    }

     */
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
                remainingTime = RemainingTime(5.seconds),
                maxTime = 30.seconds
            )
        }
    }
}