package com.infinitepower.newquiz.multi_choice_quiz.saved_questions.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.common.compose.preview.BooleanPreviewParameterProvider
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion

@Composable
@ExperimentalMaterial3Api
internal fun SavedQuestionItem(
    modifier: Modifier = Modifier,
    question: MultiChoiceQuestion,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        selected = selected,
        tonalElevation = if (selected) 8.dp else 0.dp,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        ) {
            Text(
                text = question.description,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/*
@Composable
private fun QuestionBaseCategory(
    multiChoiceQuestionCategory: MultiChoiceQuestionCategory
) {
    Icon(
        imageVector = ,
        contentDescription =
    )
}

 */

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun SavedMultiChoiceQuestionsScreenPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) selected: Boolean
) {
    NewQuizTheme {
        Surface {
            SavedQuestionItem(
                question = getBasicMultiChoiceQuestion(),
                onClick = {},
                selected = selected,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
