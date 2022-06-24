package com.infinitepower.newquiz.compose.ui.quiz

/*
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinitepower.newquiz.compose.ui.RoundCircularProgressIndicator
import com.infinitepower.newquiz.compose.ui.quiz.components.CardQuestion
import com.infinitepower.newquiz.compose.core.theme.NewQuizTheme
import com.infinitepower.newquiz.compose.model.question.Question
import com.infinitepower.newquiz.compose.model.question.QuestionStep
import com.infinitepower.newquiz.compose.model.question.getBasicQuestion
import com.infinitepower.newquiz.compose.quiz_presentation.QuizType
import com.infinitepower.newquiz.compose.quiz_presentation.components.QuizStepView
import com.infinitepower.newquiz.compose.quiz_presentation.destinations.QuizScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

private const val QUIZ_AD_UNIT_ID = "ca-app-pub-1923025671607389/6337508872"

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun QuizScreen(
    navigator: DestinationsNavigator,
    quizOptions: QuizType,
    defaultQuestionsString: String = "[]"
) {
    val quizViewModel: QuizViewModel = hiltViewModel()

    LaunchedEffect(key1 = true) {
        quizViewModel.onEvent(QuizScreenEvent.UpdateDataAndStartQuiz(quizOptions, defaultQuestionsString))
    }

    val questions = quizViewModel.questions.collectAsState(initial = emptyList())
    val currentQuestion = quizViewModel.currentQuestion.collectAsState()
    val questionPosition = quizViewModel.questionPosition.collectAsState()
    val selectedPosition = quizViewModel.selectedPosition.collectAsState()
    val quizSteps = quizViewModel.questionSteps.collectAsState()

    val progress = quizViewModel.remainingTime.collectAsState(initial = 0)
    val animatedProgressText by animateIntAsState(targetValue = progress.value.toInt())
    val animatedProgressBar by animateFloatAsState(
        targetValue = progress.value.toFloat() / QUIZ_COUNTDOWN_IN_MILLIS,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    val configuration = LocalConfiguration.current

    LaunchedEffect(key1 = true) {
        quizViewModel.uiEvent.collect { event ->
            when (event) {
                is com.infinitepower.newquiz.compose.core.common.UiEvent.Navigate -> navigator.navigate(event.direction) {
                    launchSingleTop = true
                    popUpTo(QuizScreenDestination.route) {
                        inclusive = true
                    }
                }
                is com.infinitepower.newquiz.compose.core.common.UiEvent.PopBackStack -> navigator.popBackStack()
                else -> Unit
            }
        }
    }

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
                        IconButton(onClick = { navigator.popBackStack() }) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) Spacer(modifier = Modifier.height(16.dp))
                QuizFrontLayer(
                    question = currentQuestion.value,
                    questionsSize = questions.value.size,
                    questionPosition = questionPosition.value,
                    selectedPosition = selectedPosition.value,
                    quizSteps = quizSteps.value,
                    cardQuestionClick = { position -> quizViewModel.onEvent(QuizScreenEvent.OnOptionClick(position)) },
                    cardVerifyClick = { quizViewModel.onEvent(QuizScreenEvent.OnVerifyQuestionClick) },
                    configuration = configuration,
                    saveQuestionClick = { quizViewModel.onEvent(QuizScreenEvent.OnSaveButtonClick) }
                )
                QuizBannerAdView(isInEditMode = true)
            }
        }
    }
}

@Composable
fun ColumnScope.QuizFrontLayer(
    question: Question? = null,
    questionsSize: Int = 0,
    questionPosition: Int = 0,
    selectedPosition: Int,
    quizSteps: List<QuestionStep>,
    cardQuestionClick: (position: Int) -> Unit,
    cardVerifyClick: () -> Unit,
    configuration: Configuration,
    saveQuestionClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 16.dp else 0.dp
            )
            .fillMaxWidth()
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
                    cardVerifyClick = cardVerifyClick,
                    saveQuestionClick = saveQuestionClick
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
                    cardVerifyClick = cardVerifyClick,
                    saveQuestionClick = saveQuestionClick
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
    quizSteps: List<QuestionStep>,
    cardQuestionClick: (position: Int) -> Unit,
    cardVerifyClick: () -> Unit,
    saveQuestionClick: () -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
    ) {
        itemsIndexed(
            items = quizSteps,
            key = { _, step -> step.question.id }
        ) { index, step ->
            QuizStepView(questionStep = step, position = index + 1)
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
                    selected = selectedPosition == index,
                    isResults = false,
                    resultAnswerCorrect = question?.correctAns == index
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
                    TextButton(
                        onClick = saveQuestionClick,
                    ) {
                        Text(text = "Save")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
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
    quizSteps: List<QuestionStep>,
    cardQuestionClick: (position: Int) -> Unit,
    cardVerifyClick: () -> Unit,
    saveQuestionClick: () -> Unit
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
                    QuizStepView(questionStep = step, position = index + 1)
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
                        TextButton(onClick = saveQuestionClick) {
                            Text(text = "Save")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
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
                LazyColumn {
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
        }
    }
}

object QuizScreenTestTags {
    const val TEXT_TIME_LEFT = "text_time_left"
}

@Composable
@Preview(
    name = "Portrait Light",
    group = "Portrait",
    showBackground = true
)
@Preview(
    name = "Portrait Night",
    group = "Portrait",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
fun PreviewQuizFrontLayerPortrait() {
    val question = getBasicQuestion()

    NewQuizTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            PortraitQuizScreen(
                question = question,
                questionsSize = 5,
                questionPosition = 2,
                selectedPosition = 1,
                quizSteps = listOf(
                    QuestionStep.Completed(question = question, correct = false),
                    QuestionStep.Completed(question = getBasicQuestion(), correct = true),
                    QuestionStep.Current(question = getBasicQuestion()),
                    QuestionStep.NotCurrent(question = getBasicQuestion()),
                    QuestionStep.NotCurrent(question = getBasicQuestion()),
                ),
                cardQuestionClick = {},
                cardVerifyClick = {},
                saveQuestionClick = {}
            )
        }
    }
}

@Composable
@Preview(
    name = "Landscape Light",
    group = "Landscape",
    showBackground = true,
    device = "spec:shape=Normal,width=1920,height=1080,unit=px,dpi=420",
)
@Preview(
    name = "Landscape Night",
    group = "Landscape",
    showBackground = true,
    device = "spec:shape=Normal,width=1920,height=1080,unit=px,dpi=420",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
fun PreviewQuizFrontLayerLandscape() {
    val question = getBasicQuestion()

    NewQuizTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            LandscapeQuizScreen(
                question = question,
                questionsSize = 5,
                questionPosition = 2,
                selectedPosition = 1,
                quizSteps = listOf(
                    QuestionStep.Completed(question = question, correct = false),
                    QuestionStep.Completed(question = getBasicQuestion(), correct = true),
                    QuestionStep.Current(question = getBasicQuestion()),
                    QuestionStep.NotCurrent(question = getBasicQuestion()),
                    QuestionStep.NotCurrent(question = getBasicQuestion()),
                ),
                cardQuestionClick = {},
                cardVerifyClick = {},
                saveQuestionClick = {}
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun QuizBannerAdViewPreview() {
    QuizBannerAdView()
}

 */