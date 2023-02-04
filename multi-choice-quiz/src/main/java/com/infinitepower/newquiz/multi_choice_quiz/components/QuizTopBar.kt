package com.infinitepower.newquiz.multi_choice_quiz.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.compose.ui.RoundCircularProgressIndicator
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme

@Composable
internal fun QuizTopBar(
    modifier: Modifier = Modifier,
    windowHeightSizeClass: WindowHeightSizeClass,
    progressText: String,
    progressIndicatorValue: Float,
    userSignedIn: Boolean,
    currentQuestionNull: Boolean = true,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    ConstraintLayout(modifier = modifier) {
        val (btnBackRef, progressRef, btnSkipRef) = createRefs()

        BackIconButton(
            onClick = onBackClick,
            modifier = Modifier.constrainAs(btnBackRef) {
                top.linkTo(progressRef.top)
                bottom.linkTo(progressRef.bottom)
                start.linkTo(parent.start, spaceMedium)
            }
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.constrainAs(progressRef) {
                top.linkTo(parent.top, spaceMedium)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            if (windowHeightSizeClass != WindowHeightSizeClass.Compact) {
                RoundCircularProgressIndicator(
                    progress = progressIndicatorValue,
                    modifier = Modifier.size(75.dp),
                )
            }
            Text(
                text = progressText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (!currentQuestionNull && userSignedIn) {
            FilledTonalIconButton(
                onClick = onSkipClick,
                modifier = Modifier.constrainAs(btnSkipRef) {
                    top.linkTo(progressRef.top)
                    bottom.linkTo(progressRef.bottom)
                    end.linkTo(parent.end, spaceMedium)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = "Skip question",
                )
            }
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
                progressText = "0:00",
                progressIndicatorValue = 0.7f,
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