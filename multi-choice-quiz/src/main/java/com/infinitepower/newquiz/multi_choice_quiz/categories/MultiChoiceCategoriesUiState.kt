package com.infinitepower.newquiz.multi_choice_quiz.categories

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionCategory

@Keep
data class MultiChoiceCategoriesUiState(
    val categories: List<MultiChoiceQuestionCategory> = emptyList()
)
