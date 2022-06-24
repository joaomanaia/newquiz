package com.infinitepower.newquiz.compose.ui.quiz.results

/*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infinitepower.newquiz.compose.R
import com.infinitepower.newquiz.compose.quiz_presentation.QuizType
import com.infinitepower.newquiz.compose.quiz_presentation.destinations.QuizScreenDestination
import com.infinitepower.newquiz.compose.ui.RoundCircularProgressIndicator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun QuizResultsScreen(
    navigation: DestinationsNavigator,
    quizStepsString: String,
    newXP: Long
) {
    val quizResultsViewModel: QuizResultsViewModel = hiltViewModel()

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.trophy_winner)
    )

    val questionSteps = quizResultsViewModel.questionSteps.collectAsState(initial = emptyList())
    val correctQuestionRatioString = quizResultsViewModel.correctQuestionRatioString.collectAsState(initial = "")
    val correctQuestionsRatio = quizResultsViewModel.correctQuestionsRatio.collectAsState(initial = 0f)

    val quizNewXP = quizResultsViewModel.quizNewXP.collectAsState(initial = 0)
    val bonusAllQuestionsCorrect = quizResultsViewModel.bonusAllQuestionsCorrect.collectAsState(initial = 0)

    val userIsSignedIn = quizResultsViewModel.userIsSignedIn.collectAsState(initial = false)

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            item {
                BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(
                        composition = composition,
                        modifier = Modifier.aspectRatio(1f),
                        iterations = LottieConstants.IterateForever,
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillParentMaxWidth()
                ) {
                    OutlinedButton(onClick = { navigation.popBackStack() }) {
                        Text(text = "Back")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { navigation.navigate(QuizScreenDestination(quizType = QuizType.QUICK_QUIZ)) }) {
                        Text(text = "Play Again")
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item {
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start)
                ) {
                    itemsIndexed(
                        items = questionSteps.value,
                        key = { _, step -> step.question.id }
                    ) { index, step ->
                        QuizStepView(questionStep = step, position = index + 1)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Correct Answers Ratio",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(75.dp),
                    contentAlignment = Alignment.Center
                ) {
                    RoundCircularProgressIndicator(
                        progress = correctQuestionsRatio.value,
                        modifier = Modifier.size(75.dp),
                    )
                    Text(
                        text = correctQuestionRatioString.value,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillParentMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "XP",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                if (!userIsSignedIn.value) {
                    Text(
                        text = "Sign in to claim new xp",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Text(
                    text = "+${quizNewXP.value} XP All Questions",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp)
                )
                if (bonusAllQuestionsCorrect.value > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "+${bonusAllQuestionsCorrect.value} XP Bonus 100%",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(64.dp)) }
        }
    }
}

 */