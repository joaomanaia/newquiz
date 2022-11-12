package com.infinitepower.newquiz.multi_choice_quiz.results

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.multi_choice_quiz.MultiChoiceQuizType
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.multi_choice_quiz.*
import com.infinitepower.newquiz.multi_choice_quiz.components.CardQuestionAnswers
import com.infinitepower.newquiz.multi_choice_quiz.components.QuizStepViewRow
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.infinitepower.newquiz.core.R as CoreR
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Composable
@Destination
fun MultiChoiceQuizResultsScreen(
    questionStepsStr: String,
    byInitialQuestions: Boolean = false,
    category: Int? = null,
    difficulty: String? = null,
    type: MultiChoiceQuizType,
    navigator: DestinationsNavigator
) {
    val questionSteps: List<MultiChoiceQuestionStep.Completed> = remember {
        Json.decodeFromString(questionStepsStr)
    }

    val initialQuestions = remember(questionSteps) {
        ArrayList(questionSteps.map(MultiChoiceQuestionStep::question))
    }

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("MultiChoiceResultsScreen")
    }

    MultiChoiceQuizResultsScreenImpl(
        questionSteps = questionSteps,
        onBackClick = navigator::popBackStack,
        onPlayAgainClick = {
            navigator.navigate(
                MultiChoiceQuizScreenDestination(
                    initialQuestions = if (byInitialQuestions) initialQuestions else ArrayList(),
                    category = category ?: -1,
                    difficulty = difficulty,
                    type = type
                )
            )
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MultiChoiceQuizResultsScreenImpl(
    questionSteps: List<MultiChoiceQuestionStep.Completed>,
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(spaceMedium)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            Spacer(modifier = Modifier.height(spaceLarge))
            Text(
                text = stringResource(
                    id = CoreR.string.results_screen,
                    "${questionSteps.countCorrectQuestions()}/${questionSteps.size}"
                ),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(spaceLarge))
            QuizStepViewRow(
                modifier = Modifier.fillMaxWidth(),
                questionSteps = questionSteps,
                isResultsScreen = true,
                onClick = { index, _ ->
                    setQuestionDialog(index)
                }
            )
            Spacer(modifier = Modifier.weight(1f))
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
                            resultsSelectedAnswer = SelectedAnswer.fromIndex(question.correctAns),
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
@PreviewNightLight
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

    NewQuizTheme {
        Surface {
            MultiChoiceQuizResultsScreenImpl(
                questionSteps = questionSteps,
                onBackClick = {},
                onPlayAgainClick = {}
            )
        }
    }
}