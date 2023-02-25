package com.infinitepower.newquiz.multi_choice_quiz

import androidx.annotation.Keep
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.common.viewmodel.NavEvent
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.SelectedAnswer
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
import com.infinitepower.newquiz.multi_choice_quiz.components.CardQuestionAnswers
import com.infinitepower.newquiz.multi_choice_quiz.components.MultiChoiceQuizContainer
import com.infinitepower.newquiz.multi_choice_quiz.components.QuizStepViewRow
import com.infinitepower.newquiz.multi_choice_quiz.components.QuizTopBar
import com.infinitepower.newquiz.multi_choice_quiz.components.dialog.NoDiamondsDialog
import com.infinitepower.newquiz.multi_choice_quiz.components.dialog.SkipQuestionDialog
import com.infinitepower.newquiz.core.R as CoreR
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate

internal const val MULTI_CHOICE_QUIZ_COUNTDOWN_IN_MILLIS = 30000L

@Keep
data class MultiChoiceQuizScreenNavArg(
    val initialQuestions: ArrayList<MultiChoiceQuestion> = arrayListOf(),
    val category: MultiChoiceBaseCategory = MultiChoiceBaseCategory.Normal(),
    val difficulty: String? = null,
    val mazeItemId: String? = null
)

@Composable
@Destination(
    navArgsDelegate = MultiChoiceQuizScreenNavArg::class,
    deepLinks = [
        DeepLink(uriPattern = "newquiz://quickquiz")
    ]
)
@OptIn(ExperimentalMaterial3Api::class)
fun MultiChoiceQuizScreen(
    navigator: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: QuizScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel
            .navEvent
            .collect { event ->
                when (event) {
                    is NavEvent.Navigate -> {
                        navigator.navigate(event.direction) {
                            navigator.currentDestination?.route?.let { route ->
                                launchSingleTop = true
                                popUpTo(route) {
                                    inclusive = true
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }
    }

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("MultiChoiceScreen")
    }

    MultiChoiceQuizScreenImpl(
        onBackClick = navigator::popBackStack,
        windowSizeClass = windowSizeClass,
        uiState = uiState,
        onEvent = viewModel::onEvent
    )

    if (uiState.userDiamonds == 0) {
        NoDiamondsDialog { viewModel.onEvent(MultiChoiceQuizScreenUiEvent.CleanUserSkipQuestionDiamonds) }
    } else if (uiState.userDiamonds > 0) {
        SkipQuestionDialog(
            userDiamonds = uiState.userDiamonds,
            onSkipClick = { viewModel.onEvent(MultiChoiceQuizScreenUiEvent.SkipQuestion) },
            onDismissClick = { viewModel.onEvent(MultiChoiceQuizScreenUiEvent.CleanUserSkipQuestionDiamonds) }
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun MultiChoiceQuizScreenImpl(
    uiState: MultiChoiceQuizScreenUiState,
    windowSizeClass: WindowSizeClass,
    onBackClick: () -> Unit,
    onEvent: (event: MultiChoiceQuizScreenUiEvent) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val widthCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val currentQuestion = uiState.currentQuestionStep?.question

    MultiChoiceQuizContainer(
        modifier = Modifier.fillMaxSize(),
        windowSizeClass = windowSizeClass,
        topBarContent = {
            QuizTopBar(
                remainingTime = uiState.remainingTime,
                windowHeightSizeClass = windowSizeClass.heightSizeClass,
                userSignedIn = uiState.userSignedIn,
                onBackClick = onBackClick,
                onSkipClick = { onEvent(MultiChoiceQuizScreenUiEvent.GetUserSkipQuestionDiamonds) },
                modifier = Modifier.fillMaxWidth(),
                currentQuestionNull = uiState.currentQuestionStep == null
            )
        },
        stepsContent = {
            QuizStepViewRow(
                questionSteps = uiState.questionSteps,
                isResultsScreen = false
            )
        },
        questionPositionContent = {
            Text(
                text = uiState.getQuestionPositionFormatted(),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        questionDescriptionContent = {
            if (currentQuestion != null) {
                Text(
                    text = currentQuestion.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        questionImageContent = {
            currentQuestion?.imageUrl?.let { imageUrl ->
                val imageScale = if (currentQuestion.category == MultiChoiceBaseCategory.Logo) {
                    ContentScale.FillHeight
                } else ContentScale.Crop

                val maxWidth = if (widthCompact) 1f else 0.4f

                Spacer(modifier = Modifier.height(spaceMedium))
                Box(
                    modifier = Modifier.fillMaxWidth(maxWidth)
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Image",
                        modifier = Modifier
                            .aspectRatio(16 / 9f)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = imageScale
                    )
                }
            }
        },
        answersContent = {
            CardQuestionAnswers(
                answers = currentQuestion?.answers.orEmpty(),
                selectedAnswer = uiState.selectedAnswer,
                onOptionClick = { answer ->
                    onEvent(MultiChoiceQuizScreenUiEvent.SelectAnswer(answer))
                }
            )
        },
        actionButtonsContent = {
            if (currentQuestion != null) {
                RowActionButtons(
                    answerSelected = uiState.selectedAnswer.isSelected,
                    questionSaved = uiState.questionSaved,
                    onVerifyQuestionClick = { onEvent(MultiChoiceQuizScreenUiEvent.VerifyAnswer) },
                    onSaveQuestionClick = { onEvent(MultiChoiceQuizScreenUiEvent.SaveQuestion) }
                )
            }
        }
    )
}

@Composable
private fun RowActionButtons(
    modifier: Modifier = Modifier,
    answerSelected: Boolean,
    questionSaved: Boolean,
    onVerifyQuestionClick: () -> Unit,
    onSaveQuestionClick: () -> Unit,
) {
    val spaceMedium = MaterialTheme.spacing.medium

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spaceMedium, Alignment.CenterHorizontally),
        modifier = modifier.fillMaxWidth()
    ) {
        if (!questionSaved) {
            TextButton(
                onClick = onSaveQuestionClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(id = CoreR.string.save))
            }
        }
        Button(
            onClick = onVerifyQuestionClick,
            enabled = answerSelected,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(id = CoreR.string.verify))
        }
    }
}

@Composable
@AllPreviewsNightLight
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
private fun QuizScreenPreview() {
    val questionSteps = listOf(
        MultiChoiceQuestionStep.Completed(
            question = getBasicMultiChoiceQuestion(),
            correct = true
        ),
        MultiChoiceQuestionStep.Completed(
            question = getBasicMultiChoiceQuestion(),
            correct = false
        ),
        MultiChoiceQuestionStep.Current(question = getBasicMultiChoiceQuestion()),
        MultiChoiceQuestionStep.NotCurrent(question = getBasicMultiChoiceQuestion()),
    )

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(screenWidth, screenHeight))

    NewQuizTheme {
        MultiChoiceQuizScreenImpl(
            uiState = MultiChoiceQuizScreenUiState(
                questionSteps = questionSteps,
                selectedAnswer = SelectedAnswer.fromIndex((0..3).random()),
                currentQuestionIndex = 2
            ),
            windowSizeClass = windowSizeClass,
            onBackClick = {},
            onEvent = {},
        )
    }
}