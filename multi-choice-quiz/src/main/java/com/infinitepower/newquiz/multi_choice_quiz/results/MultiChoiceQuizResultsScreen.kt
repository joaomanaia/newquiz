package com.infinitepower.newquiz.multi_choice_quiz.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.SelectedAnswer
import com.infinitepower.newquiz.model.multi_choice_quiz.countCorrectQuestions
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
import com.infinitepower.newquiz.multi_choice_quiz.components.CardQuestionAnswers
import com.infinitepower.newquiz.multi_choice_quiz.components.QuizStepViewRow
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizResultsScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kotlinx.serialization.json.Json
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@Destination
fun MultiChoiceQuizResultsScreen(
    questionStepsStr: String,
    category: MultiChoiceBaseCategory,
    byInitialQuestions: Boolean = false,
    difficulty: String? = null,
    navigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {
    val questionSteps: List<MultiChoiceQuestionStep.Completed> = remember {
        Json.decodeFromString(questionStepsStr)
    }

    val initialQuestions = remember(questionSteps) {
        ArrayList(questionSteps.map(MultiChoiceQuestionStep::question))
    }

    MultiChoiceQuizResultsScreenImpl(
        questionSteps = questionSteps,
        onBackClick = navigator::popBackStack,
        onPlayAgainClick = {
            navigator.navigate(
                MultiChoiceQuizScreenDestination(
                    initialQuestions = if (byInitialQuestions) initialQuestions else ArrayList(),
                    category = category,
                    difficulty = difficulty
                )
            ) {
                popUpTo(MultiChoiceQuizResultsScreenDestination) {
                    inclusive = true
                }

                launchSingleTop = true
            }
        },
        windowHeightSizeClass = windowSizeClass.heightSizeClass
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MultiChoiceQuizResultsScreenImpl(
    questionSteps: List<MultiChoiceQuestionStep.Completed>,
    windowHeightSizeClass: WindowHeightSizeClass,
    onBackClick: () -> Unit,
    onPlayAgainClick: () -> Unit
) {
    val winnerSpec = LottieCompositionSpec.RawRes(R.raw.trophy_winner)
    val winnerLottieComposition by rememberLottieComposition(spec = winnerSpec)

    val spaceMedium = MaterialTheme.spacing.medium
    val spaceLarge = MaterialTheme.spacing.large

    val (questionDialog, setQuestionDialog) = remember {
        mutableStateOf<Int?>(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = CoreR.string.results_screen))
                }
            )
        }
    ) { innerPadding ->
        ResultsScreenContainer(
            modifier = Modifier
                .padding(innerPadding)
                .padding(spaceMedium)
                .fillMaxSize(),
            windowHeightSizeClass = windowHeightSizeClass,
            animationContent = {
                Surface(
                    modifier = Modifier.size(300.dp),
                    tonalElevation = 8.dp,
                    shape = CircleShape
                ) {
                    LottieAnimation(
                        composition = winnerLottieComposition,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(spaceMedium),
                        iterations = LottieConstants.IterateForever,
                    )
                }
            },
            resultScoreTextContent = {
                Text(
                    text = stringResource(
                        id = CoreR.string.n_correct_questions,
                        "${questionSteps.countCorrectQuestions()}/${questionSteps.size}"
                    ),
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            stepRowContent = {
                QuizStepViewRow(
                    modifier = Modifier.fillMaxWidth(),
                    questionSteps = questionSteps,
                    isResultsScreen = true,
                    onClick = { index, _ ->
                        setQuestionDialog(index)
                    }
                )
            },
            actionButtonsContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spaceMedium),
                    modifier = Modifier.padding(bottom = spaceLarge)
                ) {
                    OutlinedButton(
                        onClick = onPlayAgainClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(id = CoreR.string.play_again))
                    }
                    Button(
                        onClick = onBackClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(id = CoreR.string.back))
                    }
                }
            }
        )
    }

    if (questionDialog != null) {
        val questionStep = questionSteps[questionDialog]
        val question = questionStep.question

        AlertDialog(
            onDismissRequest = { setQuestionDialog(null) },
            title = { Text(text = question.description) },
            text = {
                LazyColumn {
                    // Question image, if exists
                    question.imageUrl?.let { imageUrl ->
                        item {
                            Spacer(modifier = Modifier.height(spaceMedium))
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Flag Image",
                                modifier = Modifier
                                    .aspectRatio(16 / 9f)
                                    .clip(MaterialTheme.shapes.medium),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(spaceMedium))
                        }
                    }

                    item {
                        CardQuestionAnswers(
                            answers = question.answers,
                            selectedAnswer = questionStep.selectedAnswer,
                            correctAnswer = SelectedAnswer.fromIndex(question.correctAns),
                            isResultsScreen = true
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { setQuestionDialog(null) }) {
                    Text(text = stringResource(id = CoreR.string.close))
                }
            }
        )
    }
}

@Composable
private fun ResultsScreenContainer(
    modifier: Modifier = Modifier,
    windowHeightSizeClass: WindowHeightSizeClass,
    animationContent: @Composable BoxScope.() -> Unit,
    resultScoreTextContent: @Composable () -> Unit,
    stepRowContent: @Composable () -> Unit,
    actionButtonsContent: @Composable () -> Unit
) {
    val spaceLarge = MaterialTheme.spacing.large

    if (windowHeightSizeClass == WindowHeightSizeClass.Compact) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                content = animationContent,
                modifier = Modifier.weight(1f)
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                resultScoreTextContent()
                Spacer(modifier = Modifier.height(spaceLarge))
                stepRowContent()
                Spacer(modifier = Modifier.weight(1f))
                actionButtonsContent()
            }
        }
    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(content = animationContent)
            Spacer(modifier = Modifier.height(spaceLarge))
            resultScoreTextContent()
            Spacer(modifier = Modifier.height(spaceLarge))
            stepRowContent()
            Spacer(modifier = Modifier.weight(1f))
            actionButtonsContent()
        }
    }
}

@Composable
@AllPreviewsNightLight
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private fun MultiChoiceQuizResultsScreenPreview() {
    val questionSteps = listOf(
        MultiChoiceQuestionStep.Completed(
            question = getBasicMultiChoiceQuestion(),
            correct = true
        ),
        MultiChoiceQuestionStep.Completed(
            question = getBasicMultiChoiceQuestion(),
            correct = false
        ),
        MultiChoiceQuestionStep.Completed(
            question = getBasicMultiChoiceQuestion(),
            correct = true
        ),
    )

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(screenWidth, screenHeight))

    NewQuizTheme {
        Surface {
            MultiChoiceQuizResultsScreenImpl(
                questionSteps = questionSteps,
                onBackClick = {},
                onPlayAgainClick = {},
                windowHeightSizeClass = windowSizeClass.heightSizeClass
            )
        }
    }
}