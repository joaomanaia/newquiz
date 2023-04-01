package com.infinitepower.newquiz.multi_choice_quiz.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.theme.spacing

@Composable
@ExperimentalMaterial3Api
internal fun MultiChoiceQuizContainer(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    answerSelected: Boolean,
    topBarContent: @Composable () -> Unit,
    stepsContent: @Composable BoxScope.() -> Unit,
    questionPositionContent: @Composable () -> Unit,
    questionDescriptionContent: @Composable () -> Unit,
    answersContent: @Composable BoxScope.() -> Unit,
    questionImageContent: (@Composable () -> Unit)? = null,
    onVerifyQuestionClick: () -> Unit
) {
    val verticalContent = windowSizeClass.heightSizeClass > WindowHeightSizeClass.Compact

    val horizontalFractionSize = remember {
        if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) 0.5f else 1f
    }

    Scaffold(
        modifier = modifier,
        topBar = topBarContent,
        floatingActionButton = {
            if (answerSelected) {
                ExtendedFloatingActionButton(
                    onClick = onVerifyQuestionClick,
                    text = {
                        Text(text = stringResource(id = R.string.verify))
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = stringResource(id = R.string.verify)
                        )
                    }
                )
            }
        }
    ) { innerPadding ->
        if (verticalContent) {
            VerticalContent(
                modifier = Modifier.padding(innerPadding),
                stepsContent = stepsContent,
                questionPositionContent = questionPositionContent,
                questionDescriptionContent = questionDescriptionContent,
                answersContent = answersContent,
                questionImageContent = questionImageContent,
                horizontalFractionSize = horizontalFractionSize
            )
        } else {
            HorizontalContent(
                modifier = Modifier.padding(innerPadding),
                stepsContent = stepsContent,
                questionPositionContent = questionPositionContent,
                questionDescriptionContent = questionDescriptionContent,
                answersContent = answersContent,
                questionImageContent = questionImageContent,
                windowSizeClass = windowSizeClass
            )
        }
    }
}

@Composable
private fun VerticalContent(
    modifier: Modifier = Modifier,
    horizontalFractionSize: Float = 1f,
    stepsContent: @Composable BoxScope.() -> Unit,
    questionPositionContent: @Composable () -> Unit,
    questionDescriptionContent: @Composable () -> Unit,
    answersContent: @Composable BoxScope.() -> Unit,
    questionImageContent: (@Composable () -> Unit)? = null,
) {
    val spaceMedium = MaterialTheme.spacing.medium

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = spaceMedium,
                end = spaceMedium,
                top = spaceMedium,
                bottom = MaterialTheme.spacing.extraLarge
            ),
            verticalArrangement = Arrangement.spacedBy(spaceMedium),
            modifier = Modifier.fillMaxWidth(horizontalFractionSize)
        ) {
            item(key = "steps") {
                Box(
                    content = stepsContent,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(vertical = spaceMedium)
                )
            }

            item { questionPositionContent() }
            item { questionDescriptionContent() }

            // Question image, if exists
            if (questionImageContent != null) {
                item {
                    questionImageContent()
                }
            }

            item {
                Box(
                    content = answersContent,
                    modifier = Modifier.fillParentMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun HorizontalContent(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    stepsContent: @Composable BoxScope.() -> Unit,
    questionPositionContent: @Composable () -> Unit,
    questionDescriptionContent: @Composable () -> Unit,
    answersContent: @Composable BoxScope.() -> Unit,
    questionImageContent: (@Composable () -> Unit)? = null,
) {
    val spaceMedium = MaterialTheme.spacing.medium
    val spaceLarge = MaterialTheme.spacing.large

    val heightCompact = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(spaceMedium)
    ) {
        val (stepsRef, descriptionRef, answersRef) = createRefs()

        // Steps content
        Box(
            modifier = Modifier.constrainAs(stepsRef) {
                width = Dimension.wrapContent

                if (heightCompact) {
                    top.linkTo(parent.top)
                    start.linkTo(descriptionRef.start)
                    end.linkTo(descriptionRef.end)
                } else {
                    top.linkTo(parent.top, spaceLarge)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            },
            content = stepsContent
        )

        // Position, description and image content
        Column(
            modifier = Modifier.constrainAs(descriptionRef) {
                if (heightCompact) {
                    top.linkTo(stepsRef.bottom, spaceLarge)
                    start.linkTo(parent.start)
                    end.linkTo(answersRef.start)
                } else {
                    top.linkTo(stepsRef.bottom, spaceLarge)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }

                width = Dimension.fillToConstraints
            }
        ) {
            questionPositionContent()
            Spacer(modifier = Modifier.height(spaceMedium))
            questionDescriptionContent()
            // Question image, if exists
            if (questionImageContent != null) {
                Spacer(modifier = Modifier.height(spaceMedium))
                questionImageContent()
            }
        }

        // Answers content
        Box(
            modifier = Modifier.constrainAs(answersRef) {
                if (heightCompact) {
                    top.linkTo(parent.top)
                    start.linkTo(descriptionRef.end)
                    end.linkTo(parent.end)
                } else {
                    top.linkTo(descriptionRef.bottom, spaceMedium)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }

                width = Dimension.fillToConstraints
            },
            content = answersContent
        )
    }
}
