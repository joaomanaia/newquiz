package com.infinitepower.newquiz.multi_choice_quiz.components.difficulty

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.theme.CustomColor
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.extendedColors
import com.infinitepower.newquiz.core.util.asString
import com.infinitepower.newquiz.core.util.model.getText
import com.infinitepower.newquiz.model.question.QuestionDifficulty

@Composable
@ExperimentalMaterial3Api
internal fun FilledCardDifficulty(
    modifier: Modifier = Modifier,
    multiChoiceQuizDifficulty: QuestionDifficulty,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val backgroundColor = when (multiChoiceQuizDifficulty) {
        is QuestionDifficulty.Easy -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Green)
        is QuestionDifficulty.Medium -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Yellow)
        is QuestionDifficulty.Hard -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Red)
    }

    val textColor = when (multiChoiceQuizDifficulty) {
        is QuestionDifficulty.Easy -> MaterialTheme.extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Green)
        is QuestionDifficulty.Medium -> MaterialTheme.extendedColors.getColorOnAccentByKey(
            key = CustomColor.Keys.Yellow
        )
        is QuestionDifficulty.Hard -> MaterialTheme.extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Red)
    }

    FilledCardDifficulty(
        modifier = modifier,
        text = multiChoiceQuizDifficulty.getText().asString(),
        containerColor = backgroundColor,
        contentColor = textColor,
        onClick = onClick,
        enabled = enabled
    )
}

@Composable
@ExperimentalMaterial3Api
internal fun FilledCardDifficulty(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color,
    contentColor: Color,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    FilledCardDifficultyContainer(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        onClick = onClick,
        enabled = enabled
    ) {
        BaseCardDifficultyContent(text = text)
    }
}

@Composable
@ExperimentalMaterial3Api
private fun FilledCardDifficultyContainer(
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        enabled = enabled,
        onClick = onClick,
        shape = MaterialTheme.shapes.large
    ) {
        content()
    }
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun CardDifficultyPreview() {
    NewQuizTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FilledCardDifficulty(
                    multiChoiceQuizDifficulty = QuestionDifficulty.Easy,
                    onClick = {}
                )
                FilledCardDifficulty(
                    multiChoiceQuizDifficulty = QuestionDifficulty.Medium,
                    onClick = {}
                )
                FilledCardDifficulty(
                    multiChoiceQuizDifficulty = QuestionDifficulty.Hard,
                    onClick = {}
                )
            }
        }
    }
}