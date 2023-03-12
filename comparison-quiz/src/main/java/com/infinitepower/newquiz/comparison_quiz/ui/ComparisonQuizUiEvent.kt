package com.infinitepower.newquiz.comparison_quiz.ui

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem

interface ComparisonQuizUiEvent {
    @Keep
    data class OnAnswerClick(
        val item: ComparisonQuizItem
    ) : ComparisonQuizUiEvent
}