package com.infinitepower.newquiz.multi_choice_quiz.components

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.common.compose.preview.BooleanPreviewParameterProvider
import com.infinitepower.newquiz.core.theme.CustomColor
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.extendedColors
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.multi_choice_quiz.SelectedAnswer

@Composable
@ExperimentalMaterial3Api
internal fun CardQuestionAnswers(
    modifier: Modifier = Modifier,
    answers: List<String>,
    selectedAnswer: SelectedAnswer,
    isResultsScreen: Boolean = false,
    resultsSelectedAnswer: SelectedAnswer = SelectedAnswer.NONE,
    onOptionClick: (selectedAnswer: SelectedAnswer) -> Unit = {}
) {
    val spaceSmall = MaterialTheme.spacing.small

    Column(
        modifier = modifier.semantics {
            contentDescription = "Answers"
        },
        verticalArrangement = Arrangement.spacedBy(spaceSmall),
    ) {
        answers.forEachIndexed { index, answer ->
            CardQuestionAnswer(
                modifier = Modifier.fillMaxWidth(),
                description = answer,
                selected = selectedAnswer.index == index,
                isResults = isResultsScreen,
                resultAnswerCorrect = resultsSelectedAnswer.index == index,
                answerCorrect = selectedAnswer == resultsSelectedAnswer,
                onClick = { onOptionClick(SelectedAnswer.fromIndex(index)) }
            )
        }
    }
}

@Composable
@ExperimentalMaterial3Api
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun CardQuestionAnswer(
    modifier: Modifier = Modifier,
    description: String,
    selected: Boolean,
    isResults: Boolean = false,
    resultAnswerCorrect: Boolean = false,
    answerCorrect: Boolean = false,
    onClick: () -> Unit
) {
    val color by animateColorAsState(
        targetValue = when {
            isResults && selected && !answerCorrect -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Red)
            isResults && resultAnswerCorrect -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Green)
            selected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surface
        }
    )

    val textColor by animateColorAsState(
        targetValue = when {
            isResults && selected && !answerCorrect ->MaterialTheme.extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Red)
            isResults && resultAnswerCorrect -> MaterialTheme.extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Green)
            selected -> MaterialTheme.colorScheme.onPrimary
            else -> MaterialTheme.colorScheme.onSurface
        }
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CircleShape,
        tonalElevation = 8.dp,
        color = color,
        onClick = onClick,
        selected = selected,
        enabled = !isResults
    ) {
        Text(
            text = description,
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun CardQuestionOptionPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) selected: Boolean
) {
    NewQuizTheme {
        Surface {
            CardQuestionAnswer(
                description = "Answer A",
                selected = selected,
                onClick = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}