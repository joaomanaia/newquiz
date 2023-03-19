package com.infinitepower.newquiz.comparison_quiz.list

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonModeByFirst
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory

@Keep
data class ComparisonQuizListScreenUiState(
    val categories: List<ComparisonQuizCategory> = emptyList(),
    val selectedMode: ComparisonModeByFirst = ComparisonModeByFirst.GREATER
)
