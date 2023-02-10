package com.infinitepower.newquiz.multi_choice_quiz.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.model.RemainingTime
import com.infinitepower.newquiz.multi_choice_quiz.MULTI_CHOICE_QUIZ_COUNTDOWN_IN_MILLIS

@Composable
internal fun QuizTopBar(
    modifier: Modifier = Modifier,
    windowHeightSizeClass: WindowHeightSizeClass,
    remainingTime: RemainingTime,
    userSignedIn: Boolean,
    currentQuestionNull: Boolean = true,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    val progressIndicatorVisible = windowHeightSizeClass > WindowHeightSizeClass.Compact

    QuizTopBarContainer(
        modifier = modifier,
        skipButtonVisible = !currentQuestionNull && userSignedIn,
        backButtonContent = { BackIconButton(onClick = onBackClick) },
        remainingTimerContent = {
            RemainingTimeComponent(
                remainingTime = remainingTime,
                maxTimeMillis = MULTI_CHOICE_QUIZ_COUNTDOWN_IN_MILLIS,
                showProgressIndicator = progressIndicatorVisible
            )
        },
        skipButtonContent = {
            FilledTonalIconButton(onClick = onSkipClick) {
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = "Skip question",
                )
            }
        }
    )
}

@Composable
private fun QuizTopBarContainer(
    modifier: Modifier = Modifier,
    skipButtonVisible: Boolean,
    backButtonContent: @Composable BoxScope.() -> Unit,
    remainingTimerContent: @Composable BoxScope.() -> Unit,
    skipButtonContent: @Composable BoxScope.() -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    ConstraintLayout(modifier = modifier) {
        val (btnBackRef, progressRef, btnSkipRef) = createRefs()

        Box(
            modifier = Modifier.constrainAs(btnBackRef) {
                top.linkTo(progressRef.top)
                bottom.linkTo(progressRef.bottom)
                start.linkTo(parent.start, spaceMedium)
            },
            content = backButtonContent,
            contentAlignment = Alignment.Center
        )

        Box(
            modifier = Modifier.constrainAs(progressRef) {
                top.linkTo(parent.top, spaceMedium)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            content = remainingTimerContent,
            contentAlignment = Alignment.Center
        )

        if (skipButtonVisible) {
            Box(
                modifier = Modifier.constrainAs(btnSkipRef) {
                    top.linkTo(progressRef.top)
                    bottom.linkTo(progressRef.bottom)
                    end.linkTo(parent.end, spaceMedium)
                },
                content = skipButtonContent,
                contentAlignment = Alignment.Center
            )
        }
    }
}

@Composable
@PreviewNightLight
private fun QuizTopBarPreview() {
    NewQuizTheme {
        Surface {
            QuizTopBar(
                windowHeightSizeClass = WindowHeightSizeClass.Medium,
                remainingTime = RemainingTime.ZERO,
                userSignedIn = true,
                onBackClick = {},
                onSkipClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                currentQuestionNull = false
            )
        }
    }
}