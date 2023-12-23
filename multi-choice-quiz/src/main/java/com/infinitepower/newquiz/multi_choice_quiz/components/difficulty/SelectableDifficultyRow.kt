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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.core.R as CoreR

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
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = spaceMedium)
    ) {
        item {
            if (selectedDifficulty == null) {
                FilledCardDifficulty(
                    text = stringResource(id = CoreR.string.random),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = {}
                )
            } else {
                OutlinedCardDifficulty(
                    text = stringResource(id = CoreR.string.random),
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
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun CardDifficultyPreview() {
    val (selectedItem, setSelectedItem) = remember {
        // When null, difficulty will be random
        mutableStateOf<QuestionDifficulty?>(QuestionDifficulty.Easy)
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
