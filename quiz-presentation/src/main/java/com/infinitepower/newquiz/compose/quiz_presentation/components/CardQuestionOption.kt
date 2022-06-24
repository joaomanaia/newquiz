package com.infinitepower.newquiz.compose.quiz_presentation.components

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.compose.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.compose.core.common.compose.preview.BooleanPreviewParameterProvider
import com.infinitepower.newquiz.compose.core.theme.NewQuizTheme
import com.infinitepower.newquiz.compose.core.theme.spacing
import com.infinitepower.newquiz.compose.quiz_presentation.SelectedAnswer

@Composable
@ExperimentalMaterial3Api
internal fun CardQuestionAnswers(
    modifier: Modifier = Modifier,
    answers: List<String>,
    selectedAnswer: SelectedAnswer,
    onOptionClick: (selectedAnswer: SelectedAnswer) -> Unit
) {
    val spaceSmall = MaterialTheme.spacing.small

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spaceSmall)
    ) {
        answers.forEachIndexed { index, answer ->
            val selected = selectedAnswer.index == index

            CardQuestionAnswer(
                modifier = Modifier.fillMaxWidth(),
                description = answer,
                selected = selected,
                onClick = { onOptionClick(SelectedAnswer.fromIndex(index)) }
            )
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun CardQuestionAnswer(
    modifier: Modifier = Modifier,
    description: String,
    selected: Boolean,
    isResults: Boolean = false,
    resultAnswerCorrect: Boolean = false,
    onClick: () -> Unit
) {
    val color = animateColorAsState(
        targetValue = when {
            selected -> MaterialTheme.colorScheme.secondary
            isResults && resultAnswerCorrect -> MaterialTheme.colorScheme.tertiary
            else -> MaterialTheme.colorScheme.surface
        }
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CircleShape,
        tonalElevation = 8.dp,
        color = color.value,
        onClick = onClick,
        selected = selected
    ) {
        Text(
            text = description,
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            style = MaterialTheme.typography.bodyLarge
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