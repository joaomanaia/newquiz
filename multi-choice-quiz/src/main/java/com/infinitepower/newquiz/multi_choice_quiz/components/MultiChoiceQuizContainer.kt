package com.infinitepower.newquiz.multi_choice_quiz.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.infinitepower.newquiz.core.theme.spacing

@Composable
@ExperimentalMaterial3Api
internal fun MultiChoiceQuizContainer(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    topBarContent: @Composable () -> Unit,
    stepsContent: @Composable BoxScope.() -> Unit,
    questionPositionContent: @Composable ColumnScope.() -> Unit,
    questionDescriptionContent: @Composable ColumnScope.() -> Unit,
    answersContent: @Composable BoxScope.() -> Unit,
    actionButtonsContent: @Composable BoxScope.() -> Unit,
    questionImageContent: (@Composable ColumnScope.() -> Unit)? = null
) {
    val spaceMedium = MaterialTheme.spacing.medium
    val spaceLarge = MaterialTheme.spacing.large

    val heightCompact = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
    val tabletSize = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
            && windowSizeClass.heightSizeClass > WindowHeightSizeClass.Compact

    Scaffold(
        modifier = modifier,
        topBar = topBarContent
    ) { innerPadding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val (stepsRef, descriptionRef, answersRef, actionButtonsRef) = createRefs()

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
                    start.linkTo(parent.start, spaceMedium)
                    top.linkTo(stepsRef.bottom, spaceLarge)

                    if (heightCompact || tabletSize) {
                        end.linkTo(answersRef.start, spaceMedium)
                    } else {
                        end.linkTo(parent.end, spaceMedium)
                    }

                    if (tabletSize) {
                        bottom.linkTo(parent.bottom)
                    }

                    width = Dimension.fillToConstraints
                }
            ) {
                questionPositionContent()
                Spacer(modifier = Modifier.height(spaceMedium))
                questionDescriptionContent()
                // Question image, if exists
                if (questionImageContent != null) questionImageContent()
            }

            // Answers content
            Box(
                modifier = Modifier.constrainAs(answersRef) {
                    end.linkTo(parent.end, spaceMedium)

                    if (heightCompact || tabletSize) {
                        top.linkTo(parent.top)
                        start.linkTo(descriptionRef.end, spaceMedium)

                        if (tabletSize) {
                            bottom.linkTo(parent.bottom)
                        }
                    } else {
                        top.linkTo(descriptionRef.bottom, spaceLarge)
                        start.linkTo(parent.start, spaceMedium)
                    }

                    width = Dimension.fillToConstraints
                },
                content = answersContent
            )

            // Action buttons content
            Box(
                modifier = Modifier.constrainAs(actionButtonsRef) {
                    top.linkTo(answersRef.bottom, spaceMedium)
                    start.linkTo(answersRef.start)
                    end.linkTo(answersRef.end)
                    width = Dimension.fillToConstraints
                },
                content = actionButtonsContent
            )
        }
    }
}