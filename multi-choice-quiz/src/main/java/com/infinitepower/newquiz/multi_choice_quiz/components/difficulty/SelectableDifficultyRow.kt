package com.infinitepower.newquiz.multi_choice_quiz.components.difficulty

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.question.QuestionDifficulty

@Composable
@ExperimentalMaterial3Api
internal fun SelectableDifficultyRow(
    modifier: Modifier = Modifier,
    selectedDifficulty: QuestionDifficulty?,
    setSelectedDifficulty: (QuestionDifficulty?) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val items = remember { QuestionDifficulty.items() }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            if (selectedDifficulty == null) {
                FilledCardDifficulty(
                    text = "Random",
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {}
                )
            } else {
                OutlinedCardDifficulty(
                    text = "Random",
                    color = MaterialTheme.colorScheme.primary,
                    onClick = { setSelectedDifficulty(null) }
                )
            }
        }

        items(
            items = items,
            key = { it.id }
        ) { item ->
            if (item == selectedDifficulty) {
                FilledCardDifficulty(
                    multiChoiceQuizDifficulty = item,
                    onClick = {}
                )
            } else {
                OutlinedCardDifficulty(
                    multiChoiceQuizDifficulty = item,
                    onClick = { setSelectedDifficulty(item) }
                )
            }
        }
    }
}


@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun CardDifficultyPreview() {
    val (selectedItem, setSelectedItem) = remember {
        // When null, difficulty will be random
        mutableStateOf<QuestionDifficulty?>(null)
    }

    NewQuizTheme {
        Surface {
            SelectableDifficultyRow(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                selectedDifficulty = selectedItem,
                setSelectedDifficulty = setSelectedItem
            )
        }
    }
}
