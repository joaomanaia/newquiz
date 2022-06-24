package com.infinitepower.newquiz.compose.quiz_presentation

import android.content.res.Configuration
import androidx.annotation.Keep
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinitepower.newquiz.compose.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.compose.core.theme.NewQuizTheme
import com.infinitepower.newquiz.compose.core.theme.spacing
import com.infinitepower.newquiz.compose.model.question.Question
import com.infinitepower.newquiz.compose.model.question.QuestionStep
import com.infinitepower.newquiz.compose.model.question.getBasicQuestion
import com.infinitepower.newquiz.compose.quiz_presentation.components.CardQuestionAnswers
import com.infinitepower.newquiz.compose.quiz_presentation.components.QuizStepView
import com.infinitepower.newquiz.compose.quiz_presentation.components.QuizTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Keep
data class QuizScreenNavArg(
    val quizType: QuizType,
    val initialQuestions: ArrayList<Question> = arrayListOf()
)

@Composable
@Destination(navArgsDelegate = QuizScreenNavArg::class)
fun QuizScreen(
    navigator: DestinationsNavigator,
    windowWidthSizeClass: WindowWidthSizeClass,
    windowHeightSizeClass: WindowHeightSizeClass,
    viewModel: QuizScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    QuizScreenImpl(
        onBackClick = navigator::popBackStack,
        windowWidthSizeClass = windowWidthSizeClass,
        windowHeightSizeClass = windowHeightSizeClass,
        uiState = uiState
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun QuizScreenImpl(
    uiState: QuizScreenUiState,
    windowWidthSizeClass: WindowWidthSizeClass,
    windowHeightSizeClass: WindowHeightSizeClass,
    onBackClick: () -> Unit,
) {
    val spaceMedium = MaterialTheme.spacing.medium
    val spaceLarge = MaterialTheme.spacing.large

    Surface {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topBarRef, questionStepsRef, questionDataRef, answersRef, quizButtonsRef) = createRefs()

            QuizTopBar(
                progressText = "0:00",
                windowHeightSizeClass = windowHeightSizeClass,
                progressIndicatorValue = 0.7f,
                onBackClick = onBackClick,
                modifier = Modifier.constrainAs(topBarRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )

            LazyRow(
                modifier = Modifier.constrainAs(questionStepsRef) {
                    if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
                        end.linkTo(parent.end)
                        top.linkTo(topBarRef.bottom, spaceMedium)
                    } else {
                        end.linkTo(answersRef.start, spaceMedium)
                        top.linkTo(answersRef.top)
                    }

                    start.linkTo(parent.start)
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
            ) {
                itemsIndexed(
                    items = uiState.questionSteps,
                    key = { _, step -> step.question.id }
                ) { index, step ->
                    val position = index + 1

                    QuizStepView(
                        questionStep = step,
                        position = position,
                        enabled = false
                    )
                }
            }

            Column(
                modifier = Modifier.constrainAs(questionDataRef) {
                    if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
                        end.linkTo(parent.end, spaceMedium)
                    } else {
                        end.linkTo(answersRef.start, spaceMedium)
                    }

                    top.linkTo(questionStepsRef.bottom, spaceLarge)
                    start.linkTo(parent.start, spaceMedium)
                    width = Dimension.fillToConstraints
                }
            ) {
                if (uiState.currentQuestion != null) {
                    Text(
                        text = "Question ${uiState.questionPositionFormatted}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(spaceMedium))
                    Text(
                        text = uiState.currentQuestion.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            CardQuestionAnswers(
                answers = uiState.currentQuestion?.answers.orEmpty(),
                selectedAnswer = uiState.selectedAnswer,
                onOptionClick = {},
                modifier = Modifier.constrainAs(answersRef) {
                    if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
                        top.linkTo(questionDataRef.bottom, spaceMedium)
                        start.linkTo(parent.start, spaceMedium)
                    } else {
                        top.linkTo(topBarRef.bottom, spaceMedium)
                        bottom.linkTo(parent.bottom, spaceMedium)
                        start.linkTo(questionDataRef.end, spaceMedium)
                    }

                    end.linkTo(parent.end, spaceMedium)
                    width = Dimension.fillToConstraints
                }
            )

            Row(
                modifier = Modifier.constrainAs(quizButtonsRef) {
                    if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
                        start.linkTo(parent.start, spaceMedium)
                        end.linkTo(parent.end, spaceMedium)
                        top.linkTo(answersRef.bottom, spaceLarge)
                    } else {
                        start.linkTo(parent.start, spaceMedium)
                        end.linkTo(answersRef.start, spaceMedium)
                        top.linkTo(questionDataRef.bottom, spaceMedium)
                    }
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spaceMedium, Alignment.CenterHorizontally),
            ) {
                Button(
                    onClick = {},
                    enabled = uiState.currentQuestion != null
                ) {
                    Text(text = "Verify")
                }
            }
        }
    }
}

@Composable
@PreviewNightLight
private fun QuizScreenPreviewWidthCompact() {
    val uiState = remember {
        QuizScreenUiState(
            questionSteps = listOf(
                QuestionStep.Completed(
                    question = getBasicQuestion(),
                    correct = true
                ),
                QuestionStep.Completed(
                    question = getBasicQuestion(),
                    correct = false
                ),
                QuestionStep.Current(question = getBasicQuestion()),
                QuestionStep.NotCurrent(question = getBasicQuestion()),
            ),
            currentQuestion = getBasicQuestion(),
            selectedAnswer = SelectedAnswer.fromIndex((0..3).random())
        )
    }

    NewQuizTheme {
        QuizScreenImpl(
            uiState = uiState,
            windowWidthSizeClass = WindowWidthSizeClass.Compact,
            windowHeightSizeClass = WindowHeightSizeClass.Medium,
            onBackClick = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    device = "spec:shape=Normal,width=674,height=841,unit=dp,dpi=480"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:shape=Normal,width=674,height=841,unit=dp,dpi=480"
)
private fun QuizScreenPreviewWidthMedium() {
    val uiState = remember {
        QuizScreenUiState(
            questionSteps = listOf(
                QuestionStep.Completed(
                    question = getBasicQuestion(),
                    correct = true
                ),
                QuestionStep.Completed(
                    question = getBasicQuestion(),
                    correct = false
                ),
                QuestionStep.Current(question = getBasicQuestion()),
                QuestionStep.NotCurrent(question = getBasicQuestion()),
            ),
            currentQuestion = getBasicQuestion(),
            selectedAnswer = SelectedAnswer.fromIndex((0..3).random())
        )
    }

    NewQuizTheme {
        QuizScreenImpl(
            uiState = uiState,
            windowWidthSizeClass = WindowWidthSizeClass.Medium,
            windowHeightSizeClass = WindowHeightSizeClass.Medium,
            onBackClick = {}
        )
    }
}