package com.infinitepower.newquiz.quiz_presentation

import android.content.res.Configuration
import androidx.annotation.Keep
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.question.Question
import com.infinitepower.newquiz.model.question.QuestionStep
import com.infinitepower.newquiz.model.question.getBasicQuestion
import com.infinitepower.newquiz.quiz_presentation.components.CardQuestionAnswers
import com.infinitepower.newquiz.quiz_presentation.components.QuizStepView
import com.infinitepower.newquiz.quiz_presentation.components.QuizTopBar
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
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun QuizScreenImpl(
    uiState: QuizScreenUiState,
    windowWidthSizeClass: WindowWidthSizeClass,
    windowHeightSizeClass: WindowHeightSizeClass,
    onBackClick: () -> Unit,
    onEvent: (event: QuizScreenUiEvent) -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = uiState.remainingTime.getRemainingPercent(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    
    Surface {
        Column(modifier = Modifier.fillMaxSize()) {
            QuizTopBar(
                progressText = uiState.remainingTime.toMinuteSecond(),
                windowHeightSizeClass = windowHeightSizeClass,
                progressIndicatorValue = animatedProgress,
                onBackClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            )

            if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
                QuizContentWidthCompact(uiState = uiState, onEvent = onEvent)
            } else {
                QuizContentWidthMedium(uiState = uiState, onEvent = onEvent)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ColumnScope.QuizContentWidthCompact(
    uiState: QuizScreenUiState,
    onEvent: (event: QuizScreenUiEvent) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium
    val spaceLarge = MaterialTheme.spacing.large

    Spacer(modifier = Modifier.height(spaceMedium))
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
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

    AnimatedVisibility(
        visible = uiState.currentQuestionStep != null
    ) {
        val currentQuestion = uiState.currentQuestionStep?.question

        Column(
            modifier = Modifier
                .padding(horizontal = spaceMedium)
                .fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.height(spaceLarge))
            if (currentQuestion != null) {
                Text(
                    text = uiState.getQuestionPositionFormatted(),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(spaceMedium))
                Text(
                    text = currentQuestion.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(spaceLarge))
            CardQuestionAnswers(
                answers = currentQuestion?.answers.orEmpty(),
                selectedAnswer = uiState.selectedAnswer,
                onOptionClick = { answer ->
                    onEvent(QuizScreenUiEvent.SelectAnswer(answer))
                }
            )
            Spacer(modifier = Modifier.height(spaceMedium))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spaceMedium, Alignment.CenterHorizontally),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    onClick = { onEvent(QuizScreenUiEvent.VerifyAnswer) },
                    enabled = uiState.selectedAnswer.isSelected
                ) {
                    Text(text = "Verify")
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ColumnScope.QuizContentWidthMedium(
    uiState: QuizScreenUiState,
    onEvent: (event: QuizScreenUiEvent) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    AnimatedVisibility(visible = uiState.currentQuestionStep != null) {
        val currentQuestion = uiState.currentQuestionStep?.question

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = spaceMedium),
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
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
                Spacer(modifier = Modifier.height(spaceMedium))
                if (currentQuestion!= null) {
                    Text(
                        text = uiState.getQuestionPositionFormatted(),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(spaceMedium))
                    Text(
                        text = currentQuestion.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(spaceMedium))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spaceMedium, Alignment.CenterHorizontally),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onEvent(QuizScreenUiEvent.VerifyAnswer) },
                        enabled = uiState.selectedAnswer.isSelected
                    ) {
                        Text(text = "Verify")
                    }
                }
            }
            CardQuestionAnswers(
                answers = currentQuestion?.answers.orEmpty(),
                selectedAnswer = uiState.selectedAnswer,
                onOptionClick = { answer ->
                    onEvent(QuizScreenUiEvent.SelectAnswer(answer))
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
@PreviewNightLight
private fun QuizScreenPreviewWidthCompact() {
    val questionSteps = listOf(
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
    )

    val uiState = remember {
        QuizScreenUiState(
            questionSteps = questionSteps,
            selectedAnswer = SelectedAnswer.fromIndex((0..3).random())
        )
    }

    NewQuizTheme {
        QuizScreenImpl(
            uiState = uiState,
            windowWidthSizeClass = WindowWidthSizeClass.Compact,
            windowHeightSizeClass = WindowHeightSizeClass.Medium,
            onBackClick = {},
            onEvent = {},
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
    val questionSteps = listOf(
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
    )

    val uiState = remember {
        QuizScreenUiState(
            questionSteps = questionSteps,
            selectedAnswer = SelectedAnswer.fromIndex((0..3).random())
        )
    }

    NewQuizTheme {
        QuizScreenImpl(
            uiState = uiState,
            windowWidthSizeClass = WindowWidthSizeClass.Medium,
            windowHeightSizeClass = WindowHeightSizeClass.Medium,
            onBackClick = {},
            onEvent = {},
        )
    }
}