package com.infinitepower.newquiz.multi_choice_quiz.components.difficulty

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.CustomColor
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.extendedColors
import com.infinitepower.newquiz.core.util.model.getText
import com.infinitepower.newquiz.model.question.QuestionDifficulty

@Composable
@ExperimentalMaterial3Api
internal fun OutlinedCardDifficulty(
    modifier: Modifier = Modifier,
    multiChoiceQuizDifficulty: QuestionDifficulty,
    onClick: () -> Unit
) {
    val color = when (multiChoiceQuizDifficulty) {
        is QuestionDifficulty.Easy -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Green)
        is QuestionDifficulty.Medium -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Yellow)
        is QuestionDifficulty.Hard -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Red)
    }

    OutlinedCardDifficulty(
        modifier = modifier,
        text = multiChoiceQuizDifficulty.getText().asString(),
        color = color,
        onClick = onClick
    )
}

@Composable
@ExperimentalMaterial3Api
internal fun OutlinedCardDifficulty(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    OutlinedCardDifficultyContainer(
        modifier = modifier,
        color = color,
        onClick = onClick,
    ) {
        BaseCardDifficultyContent(text = text)
    }
}

@Composable
@ExperimentalMaterial3Api
private fun OutlinedCardDifficultyContainer(
    modifier: Modifier = Modifier,
    color: Color,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(
            contentColor = color
        ),
        onClick = onClick,
        border = BorderStroke(1.dp, color)
    ) {
        content()
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun OutlinedCardDifficultyPreview() {
    NewQuizTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedCardDifficulty(
                    multiChoiceQuizDifficulty = QuestionDifficulty.Easy,
                    onClick = {}
                )
                OutlinedCardDifficulty(
                    multiChoiceQuizDifficulty = QuestionDifficulty.Medium,
                    onClick = {}
                )
                OutlinedCardDifficulty(
                    multiChoiceQuizDifficulty = QuestionDifficulty.Hard,
                    onClick = {}
                )
            }
        }
    }
}