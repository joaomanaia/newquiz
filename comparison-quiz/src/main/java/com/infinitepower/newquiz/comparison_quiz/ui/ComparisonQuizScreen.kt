package com.infinitepower.newquiz.comparison_quiz.ui

import android.net.Uri
import androidx.annotation.Keep
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.comparison_quiz.destinations.ComparisonQuizScreenDestination
import com.infinitepower.newquiz.comparison_quiz.ui.components.ComparisonItem
import com.infinitepower.newquiz.comparison_quiz.ui.components.GameOverContent
import com.infinitepower.newquiz.comparison_quiz.ui.components.HelperValueState
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonModeByFirst
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCurrentQuestion
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizFormatType
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.core.R as CoreR
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Keep
data class ComparisonQuizListScreenNavArg(
    val category: ComparisonQuizCategory,
    val comparisonMode: ComparisonModeByFirst = ComparisonModeByFirst.GREATER
)

@Composable
@Destination(navArgsDelegate = ComparisonQuizListScreenNavArg::class)
@OptIn(ExperimentalMaterial3Api::class)
fun ComparisonQuizScreen(
    windowSizeClass: WindowSizeClass,
    navigator: DestinationsNavigator,
    viewModel: ComparisonQuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ComparisonQuizScreenImpl(
        uiState = uiState,
        windowSizeClass = windowSizeClass,
        onEvent = viewModel::onEvent,
        onBackClick = navigator::popBackStack,
        onPlayAgainClick = {
            navigator.navigate(
                ComparisonQuizScreenDestination(
                    category = viewModel.getCategory(),
                    comparisonMode = viewModel.getComparisonMode()
                )
            ) {
                popUpTo(ComparisonQuizScreenDestination.route) {
                    inclusive = true
                }
            }
        }
    )

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("ComparisonQuizScreen")
    }
}


@Composable
@ExperimentalMaterial3Api
private fun ComparisonQuizScreenImpl(
    uiState: ComparisonQuizUiState,
    windowSizeClass: WindowSizeClass,
    onBackClick: () -> Unit = {},
    onPlayAgainClick: () -> Unit = {},
    onEvent: (event: ComparisonQuizUiEvent) -> Unit = {}
) {
    val verticalContent = windowSizeClass.heightSizeClass > WindowHeightSizeClass.Compact
            && windowSizeClass.widthSizeClass < WindowWidthSizeClass.Expanded

    when {
        uiState.currentQuestion != null && uiState.gameDescription != null && uiState.gameCategory != null -> {
            ComparisonQuizContent(
                modifier = Modifier.fillMaxSize(),
                currentQuestion = uiState.currentQuestion,
                gameDescription = uiState.gameDescription,
                questionPosition = uiState.currentPosition,
                highestPosition = uiState.highestPosition,
                verticalContent = verticalContent,
                onBackClick = onBackClick,
                onAnswerClick = { onEvent(ComparisonQuizUiEvent.OnAnswerClick(it)) },
                gameCategory = uiState.gameCategory
            )
        }
        uiState.isGameOver -> {
            GameOverContent(
                scorePosition = uiState.currentPosition,
                highestPosition = uiState.highestPosition,
                onBackClick = onBackClick,
                onPlayAgainClick = onPlayAgainClick
            )
        }
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun ComparisonQuizContent(
    modifier: Modifier = Modifier,
    currentQuestion: ComparisonQuizCurrentQuestion,
    gameCategory: ComparisonQuizCategory,
    gameDescription: String,
    questionPosition: Int,
    highestPosition: Int,
    verticalContent: Boolean,
    onBackClick: () -> Unit,
    onAnswerClick: (ComparisonQuizItem) -> Unit
) {
    val attributionText = gameCategory.dataSourceAttribution?.text

    ComparisonQuizContainer(
        modifier = modifier.fillMaxSize(),
        verticalContent = verticalContent,
        descriptionContent = {
            Text(
                text = gameDescription,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        firstQuestionContent = {
            ComparisonItem(
                item = currentQuestion.questions.first,
                onClick = { onAnswerClick(currentQuestion.questions.first) },
                helperContentAlignment = Alignment.BottomCenter,
                helperValueState = HelperValueState.NORMAL,
                category = gameCategory
            )
        },
        secondQuestionContent = {
            ComparisonItem(
                item = currentQuestion.questions.second,
                onClick = { onAnswerClick(currentQuestion.questions.second) },
                helperContentAlignment = if (verticalContent) Alignment.TopCenter else Alignment.BottomCenter,
                helperValueState = HelperValueState.HIDDEN,
                category = gameCategory
            )
        },
        midContent = {
            ComparisonMidContent(
                questionPosition = questionPosition,
                highestPosition = highestPosition,
                heightCompact = verticalContent
            )
        },
        backIconContent = { BackIconButton(onClick = onBackClick) },
        attributionText = attributionText?.let { text ->
            {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    )
}

@Composable
fun ComparisonQuizContainer(
    modifier: Modifier = Modifier,
    verticalContent: Boolean,
    backIconContent: @Composable () -> Unit,
    descriptionContent: @Composable () -> Unit,
    firstQuestionContent: @Composable BoxScope.() -> Unit,
    secondQuestionContent: @Composable BoxScope.() -> Unit,
    midContent: @Composable () -> Unit,
    attributionText: (@Composable () -> Unit)? = null
) {
    val spaceMedium = MaterialTheme.spacing.medium

    if (verticalContent) {
        Column(
            modifier = modifier
                .padding(spaceMedium)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                backIconContent()
                Spacer(modifier = Modifier.width(spaceMedium))
                descriptionContent()
            }
            Spacer(modifier = Modifier.height(spaceMedium))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                content = firstQuestionContent
            )
            Spacer(modifier = Modifier.height(spaceMedium))
            midContent()
            Spacer(modifier = Modifier.height(spaceMedium))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                content = secondQuestionContent
            )
            attributionText?.let { attribution ->
                Spacer(modifier = Modifier.height(spaceMedium))
                attribution()
            }
        }
    } else {
        Column(
            modifier = modifier
                .padding(spaceMedium)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                backIconContent()
                Spacer(modifier = Modifier.width(spaceMedium))
                descriptionContent()
            }
            Spacer(modifier = Modifier.height(spaceMedium))
            Row(
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    content = firstQuestionContent
                )
                Spacer(modifier = Modifier.width(spaceMedium))
                midContent()
                Spacer(modifier = Modifier.width(spaceMedium))
                Box(
                    modifier = Modifier.weight(1f),
                    content = secondQuestionContent
                )
            }
            attributionText?.let { attribution ->
                Spacer(modifier = Modifier.height(spaceMedium))
                attribution()
            }
        }
    }
}

@Composable
fun ComparisonMidContent(
    modifier: Modifier = Modifier,
    questionPosition: Int,
    highestPosition: Int,
    heightCompact: Boolean
) {
    ComparisonMidContainer(
        modifier = modifier,
        verticalContent = heightCompact,
        currentPositionContent = {
            Text(
                text = stringResource(id = CoreR.string.position_n, questionPosition),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        },
        highestPositionContent = {
            Text(
                text = stringResource(id = CoreR.string.highest_n, highestPosition),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        },
        midContent = {
            Surface(
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary,
                tonalElevation = 2.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = CoreR.string.or).uppercase(),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    )
}

@Composable
fun ComparisonMidContainer(
    modifier: Modifier = Modifier,
    verticalContent: Boolean,
    currentPositionContent: @Composable () -> Unit,
    highestPositionContent: @Composable () -> Unit,
    midContent: @Composable () -> Unit
) {
    if (verticalContent) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            currentPositionContent()
            midContent()
            highestPositionContent()
        }
    } else {
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            currentPositionContent()
            midContent()
            highestPositionContent()
        }
    }
}

@Composable
@AllPreviewsNightLight
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
private fun ComparisonQuizScreenPreview() {
    val question1 = ComparisonQuizItem(
        title = "NewQuiz",
        value = 3245.0,
        imgUri = Uri.EMPTY
    )

    val question2 = ComparisonQuizItem(
        title = "NewSocial",
        value = 23445.0,
        imgUri = Uri.EMPTY
    )

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(screenWidth, screenHeight))

    NewQuizTheme {
        Surface {
            ComparisonQuizScreenImpl(
                uiState = ComparisonQuizUiState(
                    currentQuestion = ComparisonQuizCurrentQuestion(
                        questions = question1 to question2
                    ),
                    gameDescription = "Which one is more popular?",
                    gameCategory = ComparisonQuizCategory(
                        title = "Social",
                        description = "Social media",
                        imageUrl = "",
                        id = "social",
                        questionDescription = ComparisonQuizCategory.QuestionDescription(
                            greater = "Which one is more popular?",
                            less = "Which one is less popular?"
                        ),
                        dataSourceAttribution = ComparisonQuizCategory.DataSourceAttribution(
                            text = "Data from NewQuiz"
                        ),
                        formatType = ComparisonQuizFormatType.Number
                    )
                ),
                windowSizeClass = windowSizeClass
            )
        }
    }
}
