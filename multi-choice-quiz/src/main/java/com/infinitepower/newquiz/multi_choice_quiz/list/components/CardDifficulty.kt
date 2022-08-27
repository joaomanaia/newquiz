package com.infinitepower.newquiz.multi_choice_quiz.list.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SignalCellularAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.CustomColor
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.extendedColors
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.data.local.multi_choice_quiz.MultiChoiceQuizDifficulty

@Composable
@ExperimentalMaterial3Api
internal fun CardDifficulty(
    modifier: Modifier = Modifier,
    multiChoiceQuizDifficulty: MultiChoiceQuizDifficulty,
    onClick: () -> Unit
) {
    val backgroundColor = when (multiChoiceQuizDifficulty) {
        is MultiChoiceQuizDifficulty.Easy -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Green)
        is MultiChoiceQuizDifficulty.Medium -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Yellow)
        is MultiChoiceQuizDifficulty.Hard -> MaterialTheme.extendedColors.getColorAccentByKey(key = CustomColor.Keys.Red)
    }

    val textColor = when (multiChoiceQuizDifficulty) {
        is MultiChoiceQuizDifficulty.Easy -> MaterialTheme.extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Green)
        is MultiChoiceQuizDifficulty.Medium -> MaterialTheme.extendedColors.getColorOnAccentByKey(
            key = CustomColor.Keys.Yellow
        )
        is MultiChoiceQuizDifficulty.Hard -> MaterialTheme.extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Red)
    }

    CardDifficultyImpl(
        modifier = modifier,
        text = stringResource(id = multiChoiceQuizDifficulty.nameRes),
        backgroundColor = backgroundColor,
        textColor = textColor,
        onClick = onClick
    )
}

@Composable
@ExperimentalMaterial3Api
private fun CardDifficultyImpl(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(spaceMedium)) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(spaceMedium))
            Box(modifier = Modifier.padding(start = MaterialTheme.spacing.extraLarge)) {
                Icon(
                    imageVector = Icons.Rounded.SignalCellularAlt,
                    contentDescription = text,
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun CardDifficultyPreview() {
    NewQuizTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CardDifficulty(
                    multiChoiceQuizDifficulty = MultiChoiceQuizDifficulty.Easy,
                    onClick = {}
                )
                CardDifficulty(
                    multiChoiceQuizDifficulty = MultiChoiceQuizDifficulty.Medium,
                    onClick = {}
                )
                CardDifficulty(
                    multiChoiceQuizDifficulty = MultiChoiceQuizDifficulty.Hard,
                    onClick = {}
                )
            }
        }
    }
}