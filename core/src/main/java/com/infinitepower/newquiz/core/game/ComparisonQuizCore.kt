package com.infinitepower.newquiz.core.game

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonModeByFirst
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizData
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem

@Keep
data class ComparisonQuizInitialData(
    val category: ComparisonQuizCategory,
    val comparisonMode: ComparisonModeByFirst
)

interface ComparisonQuizCore : GameCore<ComparisonQuizData, ComparisonQuizInitialData> {
    fun onAnswerClicked(answer: ComparisonQuizItem)
}