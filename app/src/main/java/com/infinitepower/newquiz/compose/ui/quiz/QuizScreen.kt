package com.infinitepower.newquiz.compose.ui.quiz

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.infinitepower.newquiz.compose.model.quiz.Question
import com.infinitepower.newquiz.compose.model.quiz.QuizStep
import com.infinitepower.newquiz.compose.model.quiz.getBasicQuestion
import com.infinitepower.newquiz.compose.ui.RoundCircularProgressIndicator
import com.infinitepower.newquiz.compose.ui.quiz.components.CardQuestion
import com.infinitepower.newquiz.compose.ui.quiz.components.QuizStepView
import com.infinitepower.newquiz.compose.ui.theme.NewQuizTheme

private const val QUIZ_AD_UNIT_ID = "ca-app-pub-1923025671607389/6337508872"

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun QuizScreen(
    navController: NavController,
    quizViewModel: QuizViewModel = hiltViewModel()
) {
    val questions = quizViewModel.questions.collectAsState(initial = emptyList())
    val currentQuestion = quizViewModel.currentQuestion.collectAsState()
    val questionPosition = quizViewModel.questionPosition.collectAsState()
    val selectedPosition = quizViewModel.selectedPosition.collectAsState()
    val quizSteps = quizViewModel.quizSteps.collectAsState()

    val progress = quizViewModel.remainingTime.collectAsState(initial = 0)
    val animatedProgressText by animateIntAsState(targetValue = progress.value.toInt())
    val animatedProgressBar by animateFloatAsState(
        targetValue = progress.value.toFloat() / QUIZ_COUNTDOWN_IN_MILLIS,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    val configuration = LocalConfiguration.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            RoundCircularProgressIndicator(
                                progress = animatedProgressBar,
                                modifier = Modifier.size(75.dp),
                            )
                        }
                        Text(
                            text = animatedProgressText.toMinuteSecond(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.testTag(QuizScreenTestTags.TEXT_TIME_LEFT)
                        )
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    }
                },
                modifier = Modifier
                    .height(if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 100.dp else 40.dp)
                    .padding(top = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 16.dp else 8.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) Spacer(modifier = Modifier.height(16.dp))
            QuizFrontLayer(
                question = currentQuestion.value,
                questionsSize = questions.value.size,
                questionPosition = questionPosition.value,
                selectedPosition = selectedPosition.value,
                quizSteps = quizSteps.value,
                cardQuestionClick = { position ->
                    quizViewModel.updateSelectedPosition(position)
                },
                cardVerifyClick = {
                    quizViewModel.verifyQuestion()
                },
                configuration = configuration
            )
            QuizBannerAdView(isInEditMode = true)
        }
    }
}

@Composable
fun ColumnScope.QuizFrontLayer(
    question: Question? = null,
    questionsSize: Int = 0,
    questionPosition: Int = 0,
    selectedPosition: Int,
    quizSteps: List<QuizStep>,
    cardQuestionClick: (position: Int) -> Unit,
    cardVerifyClick: () -> Unit,
    configuration: Configuration
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 16.dp else 0.dp
            ).fillMaxWidth()
    ) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                LandscapeQuizScreen(
                    question = question,
                    questionsSize = questionsSize,
                    questionPosition = questionPosition,
                    selectedPosition = selectedPosition,
                    quizSteps = quizSteps,
                    cardQuestionClick = cardQuestionClick,
                    cardVerifyClick = cardVerifyClick
                )
            }
            else -> {
                PortraitQuizScreen(
                    question = question,
                    questionsSize = questionsSize,
                    questionPosition = questionPosition,
                    selectedPosition = selectedPosition,
                    quizSteps = quizSteps,
                    cardQuestionClick = cardQuestionClick,
                    cardVerifyClick = cardVerifyClick
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.PortraitQuizScreen(
    question: Question? = null,
    questionsSize: Int = 0,
    questionPosition: Int = 0,
    selectedPosition: Int,
    quizSteps: List<QuizStep>,
    cardQuestionClick: (position: Int) -> Unit,
    cardVerifyClick: () -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        itemsIndexed(
            items = quizSteps,
            key = { _, step -> step.question.id }
        ) { index, step ->
            QuizStepView(quizStep = step, position = index + 1)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    AnimatedVisibility(visible = question != null) {
        LazyColumn {
            item(key = "question") {
                Text(
                    text = "Question ${questionPosition + 1}/$questionsSize",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item(key = "description") {
                Text(
                    text = question?.description.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            itemsIndexed(
                items = question?.options.orEmpty(),
                key = { index, option -> "${index}_$option" }
            ) { index, option ->
                CardQuestion(
                    description = option,
                    selected = selectedPosition == index
                ) {
                    cardQuestionClick(index)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item(key = "actions_buttons") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = cardVerifyClick,
                        enabled = question != null && question.options.indices.contains(
                            selectedPosition
                        )
                    ) {
                        Text(text = "Verify")
                    }
                }
            }
        }
    }
}

@Composable
private fun LandscapeQuizScreen(
    question: Question? = null,
    questionsSize: Int = 0,
    questionPosition: Int = 0,
    selectedPosition: Int,
    quizSteps: List<QuizStep>,
    cardQuestionClick: (position: Int) -> Unit,
    cardVerifyClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
                .wrapContentWidth(Alignment.Start)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(
                    items = quizSteps,
                    key = { _, step -> step.question.id }
                ) { index, step ->
                    QuizStepView(quizStep = step, position = index + 1)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = question != null) {
                Column {
                    Text(
                        text = "Question ${questionPosition + 1}/$questionsSize",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = question?.description.orEmpty(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = cardVerifyClick,
                            enabled = question != null && question.options.indices.contains(
                                selectedPosition
                            )
                        ) {
                            Text(text = "Verify")
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
                .wrapContentWidth(Alignment.End)
        ) {
            AnimatedVisibility(visible = question != null) {
                LazyColumn{
                    itemsIndexed(
                        items = question?.options.orEmpty(),
                        key = { index, option -> "${index}_$option" }
                    ) { index, option ->
                        CardQuestion(
                            description = option,
                            selected = selectedPosition == index
                        ) {
                            cardQuestionClick(index)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
@SuppressLint("MissingPermission")
fun QuizBannerAdView(
    modifier: Modifier = Modifier,
    isInEditMode: Boolean = LocalInspectionMode.current
) {
    NewQuizTheme {
        if (isInEditMode) {
            Surface(
                modifier = modifier.fillMaxWidth(),
                tonalElevation = 8.dp
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "Banner Ad",
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        } else {
            BoxWithConstraints(
                modifier = modifier.fillMaxWidth()
            ) {
                val adSizeBox = adSizeBox()

                AndroidView(
                    modifier = modifier.fillMaxWidth(),
                    factory = { context ->
                        AdView(context).apply {
                            adSize = adSizeBox
                            adUnitId = QUIZ_AD_UNIT_ID
                            loadAd(AdRequest.Builder().build())
                        }
                    }
                )
            }
        }
    }
}

@Composable
@ReadOnlyComposable
fun BoxWithConstraintsScope.adSizeBox(): AdSize {
    val width = maxWidth.value.toInt()
    val context = LocalContext.current
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, width)
}

object QuizScreenTestTags {
    const val TEXT_TIME_LEFT = "text_time_left"
}

@Composable
@Preview(showBackground = true)
fun PreviewQuizFrontLayer() {
    Column {
        QuizFrontLayer(
            question = getBasicQuestion(),
            questionsSize = 5,
            questionPosition = 2,
            selectedPosition = 1,
            quizSteps = emptyList(),
            cardQuestionClick = {},
            cardVerifyClick = {},
            configuration = LocalConfiguration.current
        )
    }
}

@Composable
@Preview(showBackground = true)
fun QuizBannerAdViewPreview() {
    QuizBannerAdView()
}