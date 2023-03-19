package com.infinitepower.newquiz.comparison_quiz.list

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonModeByFirst

interface ComparisonQuizListScreenUiEvent {
    @Keep
    data class SelectMode(
        val mode: ComparisonModeByFirst
    ) : ComparisonQuizListScreenUiEvent
}