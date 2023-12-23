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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.compose.preview.BooleanPreviewParameterProvider
import com.infinitepower.newquiz.core.theme.CustomColor
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.extendedColors
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.multi_choice_quiz.SelectedAnswer

@Composable
internal fun CardQuestionAnswers(
    modifier: Modifier = Modifier,
    answers: List<String>,
    selectedAnswer: SelectedAnswer,
    isResultsScreen: Boolean = false,
    correctAnswer: SelectedAnswer = SelectedAnswer.NONE,
    onOptionClick: (selectedAnswer: SelectedAnswer) -> Unit = {}
) {
    val spaceSmall = MaterialTheme.spacing.small

    Column(
        modifier = modifier.semantics {
            contentDescription = "Answers container"
        },
        verticalArrangement = Arrangement.spacedBy(spaceSmall)
    ) {
        answers.forEachIndexed { index, answer ->
            CardQuestionAnswer(
                modifier = Modifier.fillMaxWidth(),
                answer = answer,
                selected = selectedAnswer.index == index,
                isResults = isResultsScreen,
                resultAnswerCorrect = correctAnswer.index == index,
                answerCorrect = selectedAnswer == correctAnswer,
                onClick = { onOptionClick(SelectedAnswer.fromIndex(index)) }
            )
        }
    }
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun CardQuestionAnswer(
    modifier: Modifier = Modifier,
    answer: String,
    selected: Boolean,
    isResults: Boolean = false,
    resultAnswerCorrect: Boolean = false,
    answerCorrect: Boolean = false,
    colors: CardQuestionAnswerColors = CardQuestionAnswerDefaults.cardColors(),
    tonalElevation: Dp = CardQuestionAnswerDefaults.cardTonalElevation,
    textPadding: Dp = CardQuestionAnswerDefaults.textPadding,
    textStyle: TextStyle = CardQuestionAnswerDefaults.textStyle,
    cardShape: Shape = CardQuestionAnswerDefaults.cardShape,
    onClick: () -> Unit
) {
    val containerColor by colors.containerColor(
        isResults = isResults,
        selected = selected,
        answerCorrect = answerCorrect,
        resultAnswerCorrect = resultAnswerCorrect
    )
    val containerColorAnimated by animateColorAsState(
        targetValue = containerColor,
        label = "container color animation"
    )

    val contentColor by colors.contentColor(
        isResults = isResults,
        selected = selected,
        answerCorrect = answerCorrect,
        resultAnswerCorrect = resultAnswerCorrect
    )
    val contentColorAnimated by animateColorAsState(
        targetValue = contentColor,
        label = "content color animation"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = cardShape,
        tonalElevation = tonalElevation,
        color = containerColorAnimated,
        onClick = onClick,
        selected = selected,
        enabled = !isResults
    ) {
        Text(
            text = answer,
            modifier = Modifier.padding(textPadding),
            style = textStyle,
            color = contentColorAnimated
        )
    }
}

object CardQuestionAnswerDefaults {
    val cardTonalElevation = 8.dp

    val textPadding: Dp @Composable get() = MaterialTheme.spacing.medium

    val textStyle: TextStyle @Composable get() = MaterialTheme.typography.bodyLarge

    val cardShape = CircleShape

    @Composable
    fun cardColors(
        normalContainerColor: Color = MaterialTheme.colorScheme.surface,
        normalContentColor: Color = MaterialTheme.colorScheme.onSurface,
        selectedContainerColor: Color = MaterialTheme.colorScheme.primary,
        selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary,
        correctContainerColor: Color = MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Green),
        correctContentColor: Color = MaterialTheme.extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Green),
        incorrectContainerColor: Color = MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Red),
        incorrectContentColor: Color = MaterialTheme.extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Red)
    ): CardQuestionAnswerColors = CardQuestionAnswerColors(
        normalContainerColor = normalContainerColor,
        normalContentColor = normalContentColor,
        selectedContainerColor = selectedContainerColor,
        selectedContentColor = selectedContentColor,
        correctContainerColor = correctContainerColor,
        correctContentColor = correctContentColor,
        incorrectContainerColor = incorrectContainerColor,
        incorrectContentColor = incorrectContentColor
    )
}

@Immutable
class CardQuestionAnswerColors internal constructor(
    private val normalContainerColor: Color,
    private val normalContentColor: Color,
    private val selectedContainerColor: Color,
    private val selectedContentColor: Color,
    private val correctContainerColor: Color,
    private val correctContentColor: Color,
    private val incorrectContainerColor: Color,
    private val incorrectContentColor: Color,
) {
    @Composable
    internal fun containerColor(
        isResults: Boolean,
        selected: Boolean,
        answerCorrect: Boolean,
        resultAnswerCorrect: Boolean
    ): State<Color> {
        return rememberUpdatedState(
            newValue = when {
                isResults && selected && !answerCorrect -> incorrectContainerColor
                isResults && resultAnswerCorrect -> correctContainerColor
                selected -> selectedContainerColor
                else -> normalContainerColor
            }
        )
    }

    @Composable
    internal fun contentColor(
        isResults: Boolean,
        selected: Boolean,
        answerCorrect: Boolean,
        resultAnswerCorrect: Boolean
    ): State<Color> {
        return rememberUpdatedState(
            newValue = when {
                isResults && selected && !answerCorrect -> incorrectContentColor
                isResults && resultAnswerCorrect -> correctContentColor
                selected -> selectedContentColor
                else -> normalContentColor
            }
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is CardQuestionAnswerColors) return false

        if (normalContainerColor != other.normalContainerColor) return false
        if (normalContentColor != other.normalContentColor) return false

        if (incorrectContainerColor != other.incorrectContainerColor) return false
        if (incorrectContentColor != other.incorrectContentColor) return false

        if (correctContainerColor != other.correctContainerColor) return false
        if (correctContentColor != other.correctContentColor) return false

        if (selectedContainerColor != other.selectedContainerColor) return false
        if (selectedContentColor != other.selectedContentColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = normalContainerColor.hashCode()
        result = 31 * result + normalContentColor.hashCode()
        result = 31 * result + incorrectContainerColor.hashCode()
        result = 31 * result + incorrectContentColor.hashCode()
        result = 31 * result + correctContainerColor.hashCode()
        result = 31 * result + correctContentColor.hashCode()
        result = 31 * result + selectedContainerColor.hashCode()
        result = 31 * result + selectedContentColor.hashCode()
        return result
    }
}


@Composable
@PreviewLightDark
private fun CardQuestionsPreview() {
    NewQuizTheme {
        Surface {
            CardQuestionAnswers(
                answers = listOf("A", "B", "C", "D"),
                selectedAnswer = SelectedAnswer.fromIndex(1),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun CardQuestionOptionPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) selected: Boolean
) {
    NewQuizTheme {
        Surface {
            CardQuestionAnswer(
                answer = "Answer A",
                selected = selected,
                onClick = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}