package com.infinitepower.newquiz.multi_choice_quiz.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.animationsEnabled
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.RemainingTimeComponent
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.model.RemainingTime
import com.infinitepower.newquiz.multi_choice_quiz.MULTI_CHOICE_QUIZ_COUNTDOWN_TIME
import com.infinitepower.newquiz.core.R as CoreR

@Composable
internal fun QuizTopBar(
    modifier: Modifier = Modifier,
    windowHeightSizeClass: WindowHeightSizeClass,
    remainingTime: RemainingTime,
    skipsAvailable: Boolean,
    questionSaved: Boolean,
    currentQuestionNull: Boolean = true,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val progressIndicatorVisible = windowHeightSizeClass > WindowHeightSizeClass.Compact

    QuizTopBarContainer(
        modifier = modifier,
        backButtonContent = { BackIconButton(onClick = onBackClick) },
        remainingTimerContent = {
            if (!remainingTime.isZero()) {
                RemainingTimeComponent(
                    remainingTime = remainingTime,
                    maxTime = MULTI_CHOICE_QUIZ_COUNTDOWN_TIME,
                    showProgressIndicator = progressIndicatorVisible,
                    animationsEnabled = MaterialTheme.animationsEnabled.multiChoice
                )
            }
        },
        onSkipClick = onSkipClick,
        onSaveClick = onSaveClick,
        skipsAvailable = skipsAvailable,
        questionSaved = questionSaved,
        currentQuestionNull = currentQuestionNull
    )
}

@Composable
private fun QuizTopBarContainer(
    modifier: Modifier = Modifier,
    skipsAvailable: Boolean,
    questionSaved: Boolean,
    currentQuestionNull: Boolean = true,
    backButtonContent: @Composable BoxScope.() -> Unit,
    remainingTimerContent: @Composable BoxScope.() -> Unit,
    onSkipClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    var moreOptionsPopupExpanded by remember { mutableStateOf(false) }

    ConstraintLayout(modifier = modifier) {
        val (btnBackRef, progressRef, btnMoreOptions) = createRefs()

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

        if (!currentQuestionNull && (skipsAvailable || !questionSaved)) {
            Box(
                modifier = Modifier.constrainAs(btnMoreOptions) {
                    top.linkTo(progressRef.top)
                    bottom.linkTo(progressRef.bottom)
                    end.linkTo(parent.end, spaceMedium)
                },
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { moreOptionsPopupExpanded = true }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = stringResource(CoreR.string.more_options)
                    )
                }

                DropdownMenu(
                    expanded = moreOptionsPopupExpanded,
                    onDismissRequest = { moreOptionsPopupExpanded = false }
                ) {
                    if (skipsAvailable) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(CoreR.string.skip)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.SkipNext,
                                    contentDescription = stringResource(CoreR.string.skip)
                                )
                            },
                            onClick = onSkipClick
                        )
                    }
                    if (!questionSaved) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(CoreR.string.save)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Save,
                                    contentDescription = stringResource(CoreR.string.save)
                                )
                            },
                            onClick = onSaveClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun QuizTopBarPreview() {
    NewQuizTheme {
        Surface {
            QuizTopBar(
                windowHeightSizeClass = WindowHeightSizeClass.Medium,
                remainingTime = RemainingTime.ZERO,
                skipsAvailable = true,
                onBackClick = {},
                onSkipClick = {},
                onSaveClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                currentQuestionNull = false,
                questionSaved = false
            )
        }
    }
}