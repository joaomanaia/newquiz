package com.infinitepower.newquiz.core.game

import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizData

interface ComparisonQuizCore : GameCore<ComparisonQuizData> {
    fun nextQuestion()
}