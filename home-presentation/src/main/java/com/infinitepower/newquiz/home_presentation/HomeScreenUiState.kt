package com.infinitepower.newquiz.home_presentation

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory

@Keep
data class HomeScreenUiState(
    val showLoginCard: Boolean = false,
    val multiChoiceRecentCategories: List<MultiChoiceCategory> = emptyList(),
    val comparisonQuizCategories: List<ComparisonQuizCategory> = emptyList(),
)
