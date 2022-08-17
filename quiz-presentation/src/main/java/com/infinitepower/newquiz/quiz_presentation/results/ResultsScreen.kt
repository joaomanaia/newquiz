package com.infinitepower.newquiz.quiz_presentation.results

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.question.*
import com.infinitepower.newquiz.quiz_presentation.components.CardQuestionAnswers
import com.infinitepower.newquiz.quiz_presentation.components.QuizStepViewRow
import com.infinitepower.newquiz.quiz_presentation.destinations.QuizScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Composable
@Destination
fun ResultsScreen(
    questionStepsStr: String,
    navigator: DestinationsNavigator
) {
    val questionSteps: List<QuestionStep.Completed> = Json.decodeFromString(questionStepsStr)

    ResultsScreenImpl(
        questionSteps = questionSteps,
        onBackClick = navigator::popBackStack,
        onPlayAgainClick = { navigator.navigate(QuizScreenDestination()) }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ResultsScreenImpl(
    questionSteps: List<QuestionStep.Completed>,
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
            SmallTopAppBar(
                title = {
                    Text(text = "Results screen")
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
                text = "${questionSteps.countCorrectQuestions()}/${questionSteps.size} correct questions",
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
                    Text(text = "Play again")
                }
                Button(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Back")
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
                CardQuestionAnswers(
                    answers = question.answers,
                    selectedAnswer = questionStep.selectedAnswer,
                    resultsSelectedAnswer = SelectedAnswer.fromIndex(question.correctAns),
                    isResultsScreen = true
                )
            },
            confirmButton = {
                TextButton(onClick = { setQuestionDialog(null) }) {
                    Text(text = "Close")
                }
            }
        )
    }
}

@Composable
@PreviewNightLight
private fun ResultsScreenPreview() {
    val questionSteps = listOf(
        QuestionStep.Completed(
            question = getBasicQuestion(),
            correct = true
        ),
        QuestionStep.Completed(
            question = getBasicQuestion(),
            correct = false
        ),
        QuestionStep.Completed(
            question = getBasicQuestion(),
            correct = true
        ),
    )

    NewQuizTheme {
        Surface {
            ResultsScreenImpl(
                questionSteps = questionSteps,
                onBackClick = {},
                onPlayAgainClick = {}
            )
        }
    }
}